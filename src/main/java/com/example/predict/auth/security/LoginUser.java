package com.example.predict.auth.security;

import com.example.predict.user.domain.UserRole;

public record LoginUser(
        Long id,
        String publicId,
        UserRole role
) {
}
