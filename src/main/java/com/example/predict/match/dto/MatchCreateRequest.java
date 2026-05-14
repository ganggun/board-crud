package com.example.predict.match.dto;

import com.example.predict.match.domain.MatchStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(description = "경기 생성/수정 요청")
public record MatchCreateRequest(
        @Schema(description = "경기 이름", example = "3학년 1반 vs 3학년 2반 축구 결승")
        @NotBlank String title,
        @Schema(description = "경기 종목 또는 카테고리", example = "축구")
        @NotBlank String category,
        @Schema(description = "A팀 이름", example = "3-1")
        @NotBlank String teamA,
        @Schema(description = "B팀 이름", example = "3-2")
        @NotBlank String teamB,
        @Schema(description = "경기 시작 시간. 이 시간이 지나면 예측할 수 없습니다.", example = "2026-05-20T14:30:00")
        @NotNull @Future LocalDateTime startTime,
        @Schema(description = "경기 상태. 생략하면 서버에서 PREDICTING으로 생성합니다.", example = "PREDICTING")
        MatchStatus status
) {
}
