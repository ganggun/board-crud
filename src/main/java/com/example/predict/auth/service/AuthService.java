package com.example.predict.auth.service;

import com.example.predict.auth.dto.LoginResponse;
import com.example.predict.auth.dto.LoginRequest;
import com.example.predict.auth.dto.SignupRequest;
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

    public LoginResponse signup(SignupRequest request) {
        User user = userService.createLocalUser(
                request.username(),
                passwordEncoder.encode(request.password()),
                request.studentId(),
                request.name(),
                request.grade(),
                request.room(),
                request.number()
        );
        return issueToken(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user;
        try {
            user = userService.getByUsername(request.username());
        } catch (EntityNotFoundException e) {
            throw new BadCredentialsException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }
        if (user.getPasswordHash() == null || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }
        return issueToken(user);
    }

    private LoginResponse issueToken(User user) {
        return new LoginResponse(
                jwtTokenProvider.createAccessToken(user),
                "Bearer",
                jwtTokenProvider.expiresIn(),
                user
        );
    }
}
