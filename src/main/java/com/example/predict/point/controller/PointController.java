package com.example.predict.point.controller;

import com.example.predict.auth.security.CurrentUser;
import com.example.predict.auth.security.LoginUser;
import com.example.predict.point.dto.PointResponse;
import com.example.predict.point.dto.RankingResponse;
import com.example.predict.point.service.PointService;
import com.example.predict.point.service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/points")
@Tag(name = "포인트", description = "내 포인트와 전체 랭킹 조회 API")
public class PointController {

    private final PointService pointService;
    private final RankingService rankingService;

    public PointController(PointService pointService, RankingService rankingService) {
        this.pointService = pointService;
        this.rankingService = rankingService;
    }

    @GetMapping("/me")
    @Operation(
            summary = "내 포인트 조회",
            description = "로그인한 사용자의 현재 포인트 잔액을 조회합니다."
    )
    public PointResponse myPoint(@Parameter(hidden = true) @CurrentUser LoginUser loginUser) {
        return new PointResponse(pointService.getPoint(loginUser.id()));
    }

    @GetMapping("/rankings")
    @Operation(
            summary = "전체 포인트 랭킹 조회",
            description = """
                    모든 사용자의 현재 포인트를 기준으로 랭킹을 조회합니다.
                    포인트가 높은 사용자부터 정렬되며, 프론트의 랭킹 화면에서 사용할 수 있습니다.
                    """
    )
    public List<RankingResponse> rankings() {
        return rankingService.rankings();
    }
}
