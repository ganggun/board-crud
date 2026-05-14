package com.example.predict.point.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

@Schema(description = "관리자 포인트 추가 지급 요청")
public record AdminPointAddRequest(
        @Schema(description = "추가 지급할 포인트. 현재 잔액에 더해집니다.", example = "500")
        @Min(1) long amount
) {
}
