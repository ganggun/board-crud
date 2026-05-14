package com.example.predict.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "아이디/비밀번호 회원가입 요청")
public record SignupRequest(
        @Schema(description = "로그인에 사용할 아이디", example = "gildong")
        @NotBlank @Size(min = 3, max = 30)
        String username,

        @Schema(description = "로그인에 사용할 비밀번호", example = "password1234")
        @NotBlank @Size(min = 8, max = 100)
        String password,

        @Schema(description = "사용자 이름", example = "홍길동")
        @NotBlank
        String name,

        @Schema(description = "학번. 학년 + 반 2자리 + 번호 2자리 형식입니다.", example = "30105")
        @NotBlank
        String studentId,

        @Schema(description = "학년", example = "3")
        @NotNull @Positive
        Integer grade,

        @Schema(description = "반", example = "1")
        @NotNull @Positive
        Integer room,

        @Schema(description = "번호", example = "5")
        @NotNull @Positive
        Integer number
) {
}
