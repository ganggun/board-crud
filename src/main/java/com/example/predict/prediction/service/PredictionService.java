package com.example.predict.prediction.service;

import com.example.predict.auth.security.LoginUser;
import com.example.predict.match.domain.Match;
import com.example.predict.match.domain.TeamSide;
import com.example.predict.match.service.MatchService;
import com.example.predict.point.service.PointService;
import com.example.predict.prediction.domain.Prediction;
import com.example.predict.prediction.dto.PredictionCreateRequest;
import com.example.predict.prediction.dto.PredictionResponse;
import com.example.predict.prediction.repository.PredictionRepository;
import com.example.predict.user.domain.User;
import com.example.predict.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class PredictionService {

    private final PredictionRepository predictionRepository;
    private final MatchService matchService;
    private final UserService userService;
    private final PointService pointService;
    private final OddsService oddsService;
    private final OddsSseService oddsSseService;

    public PredictionService(PredictionRepository predictionRepository,
                             MatchService matchService,
                             UserService userService,
                             PointService pointService,
                             OddsService oddsService,
                             OddsSseService oddsSseService) {
        this.predictionRepository = predictionRepository;
        this.matchService = matchService;
        this.userService = userService;
        this.pointService = pointService;
        this.oddsService = oddsService;
        this.oddsSseService = oddsSseService;
    }

    @Transactional
    public PredictionResponse create(Long matchId, LoginUser loginUser, PredictionCreateRequest request) {
        Match match = matchService.getById(matchId);
        if (!match.canPredict(LocalDateTime.now())) {
            throw new IllegalArgumentException("현재 예측할 수 없는 경기입니다.");
        }
        if (predictionRepository.existsByUserIdAndMatchId(loginUser.id(), matchId)) {
            throw new IllegalArgumentException("이미 예측에 참여한 경기입니다.");
        }

        User user = userService.getById(loginUser.id());
        pointService.ensureWallet(user);
        BigDecimal odds = oddsService.calculateProjectedOdds(matchId, request.selectedTeam(), request.betPoint());
        long expectedReward = BigDecimal.valueOf(request.betPoint())
                .multiply(odds)
                .setScale(0, RoundingMode.DOWN)
                .longValue();

        Prediction prediction = predictionRepository.save(new Prediction(
                user,
                match,
                request.selectedTeam(),
                request.betPoint(),
                odds,
                expectedReward
        ));
        pointService.bet(user, match, prediction, request.betPoint());

        oddsService.cacheCurrentOdds(matchId);
        Map<TeamSide, BigDecimal> currentOdds = oddsService.currentOdds(matchId);
        oddsSseService.publishOddsChanged(matchId, Map.of(
                "A", currentOdds.get(TeamSide.A),
                "B", currentOdds.get(TeamSide.B)
        ));
        return PredictionResponse.from(prediction);
    }

    @Transactional(readOnly = true)
    public List<PredictionResponse> listMyPredictions(LoginUser loginUser) {
        return predictionRepository.findByUserIdOrderByCreatedAtDesc(loginUser.id()).stream()
                .map(PredictionResponse::from)
                .toList();
    }
}
