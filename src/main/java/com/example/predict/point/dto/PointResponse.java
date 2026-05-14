package com.example.predict.point.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "내 포인트 잔액 응답")
public record PointResponse(
        @Schema(description = "현재 사용 가능한 포인트", example = "1200")
        long point
) {
}
