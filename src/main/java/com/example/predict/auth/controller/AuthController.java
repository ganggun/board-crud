package com.example.predict.auth.controller;

import com.example.predict.auth.dto.DauthLoginRequest;
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
@Tag(name = "인증", description = "DAuth 로그인과 서비스 JWT 발급 API")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/dauth")
    @Operation(
            summary = "DAuth 로그인",
            description = """
                    프론트가 DAuth에서 받은 accessToken을 서버로 전달합니다.
                    서버는 DAuth 사용자 정보를 조회한 뒤 학생 계정만 허용하고,
                    우리 서비스에서 사용할 JWT accessToken을 발급합니다.
                    이후 인증이 필요한 API는 Authorization 헤더에 Bearer 토큰을 넣어 호출합니다.
                    """
    )
    public LoginResponse loginWithDauth(@Valid @RequestBody DauthLoginRequest request) {
        return authService.loginWithDauthAccessToken(request.accessToken());
    }
}
