package com.example.predict.auth.service;

import com.example.predict.auth.dto.LoginResponse;
import com.example.predict.user.domain.User;
import com.example.predict.user.service.UserService;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AuthService {

    private final RestClient restClient;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserService userService, JwtTokenProvider jwtTokenProvider) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10_000);
        requestFactory.setReadTimeout(10_000);
        this.restClient = RestClient.builder()
                .baseUrl("https://dodam-api.b1nd.com")
                .requestFactory(requestFactory)
                .build();
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse loginWithDauthAccessToken(String dauthAccessToken) {
        DauthProfile profile = fetchDauthProfile(dauthAccessToken);
        List<String> roles = profile.roles() == null ? List.of() : profile.roles();
        if (!roles.contains("STUDENT") || profile.student() == null) {
            throw new AccessDeniedException("학생 계정만 로그인할 수 있습니다.");
        }

        User user = userService.upsertDauthStudent(profile);
        return new LoginResponse(
                jwtTokenProvider.createAccessToken(user),
                "Bearer",
                jwtTokenProvider.expiresIn(),
                user
        );
    }

    private DauthProfile fetchDauthProfile(String dauthAccessToken) {
        try {
            DauthResponse response = restClient.get()
                    .uri("/user/me")
                    .header("Authorization", "Bearer " + dauthAccessToken)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response1) -> {
                        throw new BadCredentialsException("유효하지 않은 DAuth Access Token입니다.");
                    })
                    .body(DauthResponse.class);

            if (response == null || response.data() == null) {
                throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_GATEWAY, "DAuth 응답이 올바르지 않습니다.");
            }
            return response.data();
        } catch (BadCredentialsException e) {
            throw e;
        } catch (RestClientException e) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_GATEWAY, "DAuth 서버 호출에 실패했습니다.");
        }
    }
}
