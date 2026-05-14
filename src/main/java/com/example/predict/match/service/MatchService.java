package com.example.predict.match.service;

import com.example.predict.match.domain.Match;
import com.example.predict.match.domain.MatchStatus;
import com.example.predict.match.dto.MatchCreateRequest;
import com.example.predict.match.dto.MatchResponse;
import com.example.predict.match.dto.MatchResultRequest;
import com.example.predict.match.repository.MatchRepository;
import com.example.predict.prediction.domain.Prediction;
import com.example.predict.prediction.repository.PredictionRepository;
import com.example.predict.prediction.service.OddsService;
import com.example.predict.point.service.PointService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final PredictionRepository predictionRepository;
    private final OddsService oddsService;
    private final PointService pointService;

    public MatchService(MatchRepository matchRepository,
                        PredictionRepository predictionRepository,
                        OddsService oddsService,
                        PointService pointService) {
        this.matchRepository = matchRepository;
        this.predictionRepository = predictionRepository;
        this.oddsService = oddsService;
        this.pointService = pointService;
    }

    @Transactional(readOnly = true)
    public List<MatchResponse> listActiveMatches() {
        return matchRepository.findByStatusInOrderByStartTimeAsc(List.of(
                        MatchStatus.SCHEDULED,
                        MatchStatus.PREDICTING,
                        MatchStatus.CLOSED,
                        MatchStatus.IN_PROGRESS
                )).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Match getById(Long matchId) {
        return matchRepository.findById(matchId)
                .orElseThrow(() -> new EntityNotFoundException("경기를 찾을 수 없습니다."));
    }

    @Transactional
    public MatchResponse create(MatchCreateRequest request) {
        Match match = matchRepository.save(new Match(
                request.title(),
                request.category(),
                request.teamA(),
                request.teamB(),
                request.startTime(),
                request.status() == null ? MatchStatus.PREDICTING : request.status()
        ));
        return toResponse(match);
    }

    @Transactional
    public MatchResponse update(Long matchId, MatchCreateRequest request) {
        Match match = getById(matchId);
        match.update(
                request.title(),
                request.category(),
                request.teamA(),
                request.teamB(),
                request.startTime(),
                request.status() == null ? match.getStatus() : request.status()
        );
        return toResponse(match);
    }

    @Transactional
    public void delete(Long matchId) {
        matchRepository.delete(getById(matchId));
    }

    @Transactional
    public MatchResponse finish(Long matchId, MatchResultRequest request) {
        Match match = getById(matchId);
        if (match.getStatus() == MatchStatus.FINISHED) {
            throw new IllegalArgumentException("이미 결과가 등록된 경기입니다.");
        }
        match.finish(request.winnerTeam());

        List<Prediction> predictions = predictionRepository.findByMatchId(matchId);
        for (Prediction prediction : predictions) {
            boolean correct = prediction.getSelectedTeam() == request.winnerTeam();
            long reward = correct ? prediction.getExpectedRewardPoint() : 0;
            prediction.settle(correct, reward);
            if (correct && reward > 0) {
                pointService.reward(prediction.getUser(), match, prediction, reward);
            }
        }
        return toResponse(match);
    }

    private MatchResponse toResponse(Match match) {
        Map<com.example.predict.match.domain.TeamSide, BigDecimal> odds = oddsService.currentOdds(match.getId());
        return MatchResponse.of(
                match,
                odds.get(com.example.predict.match.domain.TeamSide.A),
                odds.get(com.example.predict.match.domain.TeamSide.B)
        );
    }
}
