package com.example.predict.auth.service;

import com.example.predict.auth.security.LoginUser;
import com.example.predict.user.domain.User;
import com.example.predict.user.domain.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String secret;
    private final String expiresIn;

    public JwtTokenProvider(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expires-in}") String expiresIn
    ) {
        this.secret = secret;
        this.expiresIn = expiresIn;
    }

    public String createAccessToken(User user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(parseDuration(expiresIn));
        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("publicId", user.getPublicId())
                .claim("role", user.getRole().name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(key())
                .compact();
    }

    public LoginUser parse(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return new LoginUser(
                Long.valueOf(claims.getSubject()),
                claims.get("publicId", String.class),
                UserRole.valueOf(claims.get("role", String.class))
        );
    }

    public String expiresIn() {
        return expiresIn;
    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private Duration parseDuration(String value) {
        if (value.endsWith("d")) {
            return Duration.ofDays(Long.parseLong(value.substring(0, value.length() - 1)));
        }
        if (value.endsWith("h")) {
            return Duration.ofHours(Long.parseLong(value.substring(0, value.length() - 1)));
        }
        if (value.endsWith("m")) {
            return Duration.ofMinutes(Long.parseLong(value.substring(0, value.length() - 1)));
        }
        return Duration.ofSeconds(Long.parseLong(value));
    }
}
