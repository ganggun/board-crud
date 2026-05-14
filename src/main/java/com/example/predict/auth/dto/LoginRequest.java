package com.example.predict.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "아이디/비밀번호 로그인 요청")
public record LoginRequest(
        @Schema(description = "회원가입한 아이디", example = "gildong")
        @NotBlank
        String username,

        @Schema(description = "비밀번호", example = "password1234")
        @NotBlank
        String password
) {
}
