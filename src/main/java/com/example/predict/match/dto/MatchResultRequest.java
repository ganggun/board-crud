package com.example.predict.match.dto;

import com.example.predict.match.domain.TeamSide;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "경기 결과 등록 요청")
public record MatchResultRequest(
        @Schema(description = "실제 승리 팀", example = "A")
        @NotNull TeamSide winnerTeam
) {
}
