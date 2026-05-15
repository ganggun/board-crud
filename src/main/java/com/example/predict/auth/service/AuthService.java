package com.example.predict.auth.service;

import com.example.predict.auth.dto.LoginResponse;
import com.example.predict.auth.dto.LoginRequest;
import com.example.predict.point.service.PointService;
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
    private final PointService pointService;

    public AuthService(UserService userService,
                       JwtTokenProvider jwtTokenProvider,
                       PasswordEncoder passwordEncoder,
                       PointService pointService) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.pointService = pointService;
    }

    public LoginResponse login(LoginRequest request) {
        try {
            User user = userService.getByUsername(request.username());
            if (user.getPasswordHash() == null || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
                throw new BadCredentialsException("아이디 또는 비밀번호가 올바르지 않습니다.");
            }
            return issueToken(user);
        } catch (EntityNotFoundException e) {
            User createdUser = userService.createLocalUser(
                    request.username(),
                    passwordEncoder.encode(request.password()),
                    resolveStudentId(request),
                    resolveName(request),
                    resolvePositiveNumber(request.grade()),
                    resolvePositiveNumber(request.room()),
                    resolvePositiveNumber(request.number())
            );
            pointService.ensureWallet(createdUser);
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

    private String resolveName(LoginRequest request) {
        if (isBlank(request.name()) || "1".equals(request.name().trim())) {
            return request.username();
        }
        return request.name().trim();
    }

    private String resolveStudentId(LoginRequest request) {
        if (isPlaceholderStudentId(request.studentId())) {
            return "U" + Integer.toUnsignedString(request.username().hashCode());
        }
        return request.studentId().trim();
    }

    private boolean isPlaceholderStudentId(String studentId) {
        if (isBlank(studentId)) {
            return true;
        }
        String trimmed = studentId.trim();
        return "1".equals(trimmed) || trimmed.matches("0+");
    }

    private Integer resolvePositiveNumber(Integer value) {
        if (value == null || value <= 0) {
            return 1;
        }
        return value;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
