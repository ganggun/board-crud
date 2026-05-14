package com.example.predict.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        String bearer = "bearer";
        return new OpenAPI()
                .info(new Info()
                        .title("School Predict API")
                        .version("1.0.0")
                        .description("""
                                학교 행사 승부예측 서비스 API 문서입니다.
                                아이디/비밀번호 로그인 후 발급받은 서비스 JWT를 Swagger Authorize 버튼에 Bearer 토큰으로 등록하면 인증 API를 테스트할 수 있습니다.
                                """)
                        .contact(new Contact().name("School Predict Backend")))
                .schemaRequirement(bearer, new SecurityScheme()
                        .name(bearer)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"))
                .addSecurityItem(new SecurityRequirement().addList(bearer));
    }
}
