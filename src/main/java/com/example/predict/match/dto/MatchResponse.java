package com.example.predict.match.dto;

import com.example.predict.match.domain.Match;
import com.example.predict.match.domain.MatchStatus;
import com.example.predict.match.domain.TeamSide;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "경기 조회 응답")
public record MatchResponse(
        @Schema(description = "경기 ID", example = "1")
        Long id,
        @Schema(description = "경기 이름", example = "3학년 1반 vs 3학년 2반 축구 결승")
        String title,
        @Schema(description = "경기 종목 또는 카테고리", example = "축구")
        String category,
        @Schema(description = "A팀 이름", example = "3-1")
        String teamA,
        @Schema(description = "B팀 이름", example = "3-2")
        String teamB,
        @Schema(description = "경기 시작 시간", example = "2026-05-20T14:30:00")
        LocalDateTime startTime,
        @Schema(description = "경기 상태", example = "PREDICTING")
        MatchStatus status,
        @Schema(description = "결과 등록 후 승리 팀. 결과 등록 전에는 null입니다.", example = "A")
        TeamSide winnerTeam,
        @Schema(description = "현재 로그인 사용자가 이 경기에 예측할 수 있는 시간/상태인지 여부", example = "true")
        boolean predictable,
        @Schema(description = "현재 팀 A 승리 배당률. 예측 시점에 이 값이 Prediction에 고정 저장됩니다.", example = "1.25")
        BigDecimal teamAOdds,
        @Schema(description = "현재 팀 B 승리 배당률. 예측 시점에 이 값이 Prediction에 고정 저장됩니다.", example = "3.40")
        BigDecimal teamBOdds
) {
    public static MatchResponse of(Match match, BigDecimal teamAOdds, BigDecimal teamBOdds) {
        return new MatchResponse(
                match.getId(),
                match.getTitle(),
                match.getCategory(),
                match.getTeamA(),
                match.getTeamB(),
                match.getStartTime(),
                match.getStatus(),
                match.getWinnerTeam(),
                match.canPredict(LocalDateTime.now()),
                teamAOdds,
                teamBOdds
        );
    }
}
