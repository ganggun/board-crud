package com.example.predict.prediction.dto;

import com.example.predict.match.domain.TeamSide;
import com.example.predict.prediction.domain.Prediction;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "승부예측 조회 응답")
public record PredictionResponse(
        @Schema(description = "예측 ID", example = "1")
        Long id,
        @Schema(description = "예측한 사용자 ID", example = "10")
        Long userId,
        @Schema(description = "예측 대상 경기 ID", example = "1")
        Long matchId,
        @Schema(description = "사용자가 선택한 팀", example = "A")
        TeamSide selectedTeam,
        @Schema(description = "사용자가 건 포인트", example = "100")
        long betPoint,
        @Schema(description = "예측 시점에 고정된 배당률", example = "2.35")
        BigDecimal odds,
        @Schema(description = "맞혔을 때 받을 예상 보상. 원금 포함 betPoint * odds 값입니다.", example = "235")
        long expectedRewardPoint,
        @Schema(description = "경기 결과 등록 후 실제 지급된 포인트. 오답이면 0, 미정산이면 null입니다.", example = "235")
        Long rewardPoint,
        @Schema(description = "정답 여부. 결과 등록 전에는 null입니다.", example = "true")
        Boolean correct,
        @Schema(description = "예측 참여 시간", example = "2026-05-14T20:10:00")
        LocalDateTime createdAt
) {
    public static PredictionResponse from(Prediction prediction) {
        return new PredictionResponse(
                prediction.getId(),
                prediction.getUser().getId(),
                prediction.getMatch().getId(),
                prediction.getSelectedTeam(),
                prediction.getBetPoint(),
                prediction.getOdds(),
                prediction.getExpectedRewardPoint(),
                prediction.getRewardPoint(),
                prediction.getCorrect(),
                prediction.getCreatedAt()
        );
    }
}
