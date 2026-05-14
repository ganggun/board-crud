package com.example.predict.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // 프론트 주소
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://127.0.0.1:5500",
                "https://cher1shrxd.github.io/"

        ));

        // 쿠키/인증 헤더 허용
        config.setAllowCredentials(true);

        // 허용 HTTP 메서드
        config.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        // 허용 헤더
        config.setAllowedHeaders(List.of("*"));

        // 프론트에서 읽을 수 있는 응답 헤더
        config.setExposedHeaders(List.of(
                "Authorization",
                "Set-Cookie"
        ));

        // preflight 캐시 시간
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
