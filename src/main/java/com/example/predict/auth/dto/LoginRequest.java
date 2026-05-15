package com.example.predict.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "로그인 또는 최초 사용자 생성 요청")
public record LoginRequest(
        @Schema(description = "로그인에 사용할 아이디. 이미 있으면 로그인하고, 없으면 새 사용자를 생성합니다.", example = "gildong")
        @NotBlank @Size(min = 3, max = 30)
        String username,

        @Schema(description = "비밀번호. 새 사용자를 만들 때는 BCrypt로 해싱되어 저장됩니다.", example = "password1234")
        @NotBlank @Size(min = 8, max = 100)
        String password,

        @Schema(description = "사용자 이름. 생략하거나 1로 보내면 username을 이름으로 저장합니다.", example = "홍길동")
        String name,

        @Schema(description = "학번. 생략하거나 1로 보내면 서버가 username 기반 고유값을 생성합니다.", example = "30105")
        String studentId,

        @Schema(description = "학년. 생략하면 1로 저장합니다.", example = "1")
        Integer grade,

        @Schema(description = "반. 생략하면 1로 저장합니다.", example = "1")
        Integer room,

        @Schema(description = "번호. 생략하면 1로 저장합니다.", example = "1")
        Integer number
) {
}
