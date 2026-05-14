package com.example.predict.auth.service;

import com.example.predict.auth.dto.LoginResponse;
import com.example.predict.auth.dto.LoginRequest;
import com.example.predict.user.domain.User;
import com.example.predict.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserService userService, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request) {
        try {
            User user = userService.getByUsername(request.username());
            if (user.getPasswordHash() == null || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
                throw new BadCredentialsException("아이디 또는 비밀번호가 올바르지 않습니다.");
            }
            return issueToken(user);
        } catch (EntityNotFoundException e) {
            validateCreateFields(request);
            User createdUser = userService.createLocalUser(
                    request.username(),
                    passwordEncoder.encode(request.password()),
                    request.studentId(),
                    request.name(),
                    request.grade(),
                    request.room(),
                    request.number()
            );
            return issueToken(createdUser);
        }
    }

    private LoginResponse issueToken(User user) {
        return new LoginResponse(
                jwtTokenProvider.createAccessToken(user),
                "Bearer",
                jwtTokenProvider.expiresIn(),
                user
        );
    }

    private void validateCreateFields(LoginRequest request) {
        if (isBlank(request.name())) {
            throw new IllegalArgumentException("새 사용자를 생성하려면 name이 필요합니다.");
        }
        if (isBlank(request.studentId())) {
            throw new IllegalArgumentException("새 사용자를 생성하려면 studentId가 필요합니다.");
        }
        if (request.grade() == null || request.grade() <= 0) {
            throw new IllegalArgumentException("새 사용자를 생성하려면 grade가 필요합니다.");
        }
        if (request.room() == null || request.room() <= 0) {
            throw new IllegalArgumentException("새 사용자를 생성하려면 room이 필요합니다.");
        }
        if (request.number() == null || request.number() <= 0) {
            throw new IllegalArgumentException("새 사용자를 생성하려면 number가 필요합니다.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
