package com.example.predict.auth.controller;

import com.example.predict.auth.dto.LoginRequest;
import com.example.predict.auth.dto.LoginResponse;
import com.example.predict.auth.dto.SignupRequest;
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
@Tag(name = "인증", description = "아이디/비밀번호 로그인과 서비스 JWT 발급 API")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    @Operation(
            summary = "회원가입",
            description = """
                    자체 아이디/비밀번호 방식으로 사용자를 생성합니다.
                    지금은 DAuth 연동 없이 프론트 개발과 테스트를 빠르게 진행하기 위한 임시 회원가입 API입니다.
                    가입 성공 시 바로 서비스 JWT를 발급합니다.
                    """
    )
    public LoginResponse signup(@Valid @RequestBody SignupRequest request) {
        return authService.signup(request);
    }

    @PostMapping("/login")
    @Operation(
            summary = "아이디/비밀번호 로그인",
            description = """
                    회원가입한 아이디와 비밀번호로 로그인합니다.
                    성공하면 우리 서비스 JWT accessToken을 반환하며,
                    이후 인증 API는 Authorization: Bearer <accessToken> 헤더로 호출합니다.
                    """
    )
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

}
