package com.example.predict.auth.dto;

import com.example.predict.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "우리 서비스 JWT 로그인 응답")
public record LoginResponse(
        @Schema(description = "우리 서비스 API 호출에 사용할 JWT accessToken", example = "eyJhbGciOi...")
        String accessToken,
        @Schema(description = "토큰 타입. Authorization 헤더에는 Bearer + 공백 + accessToken 형태로 사용합니다.", example = "Bearer")
        String tokenType,
        @Schema(description = "JWT 만료 시간 설정값", example = "5d")
        String expiresIn,
        @Schema(description = "로그인한 사용자 정보")
        User user
) {
}
