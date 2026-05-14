package com.example.predict.point.service;

import com.example.predict.point.dto.RankingResponse;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankingService {

    private final PointService pointService;
    private final StringRedisTemplate redisTemplate;

    public RankingService(PointService pointService, StringRedisTemplate redisTemplate) {
        this.pointService = pointService;
        this.redisTemplate = redisTemplate;
    }

    public List<RankingResponse> rankings() {
        List<RankingResponse> rankings = pointService.rankings();
        try {
            rankings.forEach(ranking -> redisTemplate.opsForZSet()
                    .add("rankings:points", String.valueOf(ranking.userId()), ranking.point()));
        } catch (RedisConnectionFailureException ignored) {
        }
        return rankings;
    }
}
