package com.example.predict.point.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "포인트 랭킹 응답")
public record RankingResponse(
        @Schema(description = "순위", example = "1")
        int rank,
        @Schema(description = "사용자 ID", example = "10")
        Long userId,
        @Schema(description = "사용자 이름", example = "홍길동")
        String name,
        @Schema(description = "학번. 학년 + 반 2자리 + 번호 2자리 형식입니다.", example = "30105")
        String studentId,
        @Schema(description = "현재 포인트", example = "1850")
        long point
) {
}
