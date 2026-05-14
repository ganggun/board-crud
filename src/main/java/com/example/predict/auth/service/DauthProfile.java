package com.example.predict.auth.service;

import java.util.List;

public record DauthProfile(
        String publicId,
        String username,
        String name,
        String phone,
        String profileImage,
        String status,
        List<String> roles,
        DauthStudent student
) {
}
