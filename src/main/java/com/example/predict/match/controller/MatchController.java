package com.example.predict.match.controller;

import com.example.predict.match.dto.MatchResponse;
import com.example.predict.match.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/matches")
@Tag(name = "경기", description = "학생이 조회하는 경기 목록 API")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping
    @Operation(
            summary = "진행/예정 경기 목록 조회",
            description = """
                    현재 진행 예정이거나 예측 가능한 경기 목록을 시작 시간 순서로 조회합니다.
                    각 경기에는 경기 상태, 예측 가능 여부, 팀 A/B의 현재 실시간 배당률이 포함됩니다.
                    """
    )
    public List<MatchResponse> listActiveMatches() {
        return matchService.listActiveMatches();
    }
}
