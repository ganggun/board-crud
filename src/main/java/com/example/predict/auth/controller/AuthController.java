package com.example.predict.auth.controller;

import com.example.predict.auth.dto.LoginRequest;
import com.example.predict.auth.dto.LoginResponse;
import com.example.predict.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "인증", description = "아이디/비밀번호 기반 로그인 및 자동 사용자 생성 API")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(
            summary = "로그인 또는 최초 사용자 생성",
            description = """
                    아이디가 이미 있으면 비밀번호를 검증하고 로그인합니다.
                    아이디가 없으면 name, studentId, grade, room, number 값을 사용해 새 사용자를 만든 뒤 바로 로그인합니다.
                    새로 생성되는 사용자의 기본 권한은 USER입니다.
                    성공하면 우리 서비스 JWT accessToken을 반환하며 이후 인증 API는 Authorization: Bearer <accessToken> 헤더로 호출합니다.
                    """
    )
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

}
