package com.example.predict.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DAuth accessToken 로그인 요청")
public record DauthLoginRequest(
        @Schema(description = "프론트가 DAuth 로그인 후 받은 DAuth accessToken", example = "eyJhbGciOi...")
        @NotBlank String accessToken
) {
}
