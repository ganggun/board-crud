package com.example.predict.prediction.dto;

import com.example.predict.match.domain.TeamSide;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "승부예측 참여 요청")
public record PredictionCreateRequest(
        @Schema(description = "사용자가 승리할 것으로 선택한 팀", example = "A")
        @NotNull TeamSide selectedTeam,
        @Schema(description = "예측에 걸 포인트. 참여 즉시 사용자 포인트에서 차감됩니다.", example = "100")
        @Min(1) long betPoint
) {
}
