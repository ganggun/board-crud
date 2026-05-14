package com.example.predict.user.controller;

import com.example.predict.auth.security.CurrentUser;
import com.example.predict.auth.security.LoginUser;
import com.example.predict.user.domain.User;
import com.example.predict.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(name = "사용자", description = "로그인한 사용자 정보 API")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @Operation(
            summary = "내 사용자 정보 조회",
            description = """
                    JWT로 인증된 사용자의 DB 저장 정보를 조회합니다.
                    DAuth에서 가져온 이름, 학번, 학년, 반, 번호와 서버에서 관리하는 role을 확인할 수 있습니다.
                    """
    )
    public User me(@Parameter(hidden = true) @CurrentUser LoginUser loginUser) {
        return userService.getById(loginUser.id());
    }
}
