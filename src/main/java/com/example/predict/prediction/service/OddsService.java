package com.example.predict.prediction.service;

import com.example.predict.match.domain.TeamSide;
import com.example.predict.prediction.repository.PredictionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class OddsService {

    private final PredictionRepository predictionRepository;
    private final StringRedisTemplate redisTemplate;
    private final BigDecimal minOdds;
    private final BigDecimal maxOdds;

    public OddsService(PredictionRepository predictionRepository,
                       StringRedisTemplate redisTemplate,
                       @Value("${app.odds.min}") BigDecimal minOdds,
                       @Value("${app.odds.max}") BigDecimal maxOdds) {
        this.predictionRepository = predictionRepository;
        this.redisTemplate = redisTemplate;
        this.minOdds = minOdds;
        this.maxOdds = maxOdds;
    }

    public Map<TeamSide, BigDecimal> currentOdds(Long matchId) {
        return Map.of(
                TeamSide.A, calculateProjectedOdds(matchId, TeamSide.A, 0),
                TeamSide.B, calculateProjectedOdds(matchId, TeamSide.B, 0)
        );
    }

    public BigDecimal calculateProjectedOdds(Long matchId, TeamSide selectedTeam, long newBetPoint) {
        long total = predictionRepository.sumBetPointByMatchId(matchId) + newBetPoint;
        long side = predictionRepository.sumBetPointByMatchIdAndTeam(matchId, selectedTeam) + newBetPoint;
        if (total <= 0 || side <= 0) {
            return maxOdds;
        }

        BigDecimal odds = BigDecimal.valueOf(total)
                .divide(BigDecimal.valueOf(side), 2, RoundingMode.HALF_UP);
        return clamp(odds);
    }

    public void cacheCurrentOdds(Long matchId) {
        Map<TeamSide, BigDecimal> odds = currentOdds(matchId);
        try {
            redisTemplate.opsForHash().put("match:%d:odds".formatted(matchId), "A", odds.get(TeamSide.A).toPlainString());
            redisTemplate.opsForHash().put("match:%d:odds".formatted(matchId), "B", odds.get(TeamSide.B).toPlainString());
        } catch (RedisConnectionFailureException ignored) {
        }
    }

    private BigDecimal clamp(BigDecimal odds) {
        if (odds.compareTo(minOdds) < 0) {
            return minOdds;
        }
        if (odds.compareTo(maxOdds) > 0) {
            return maxOdds;
        }
        return odds;
    }
}
