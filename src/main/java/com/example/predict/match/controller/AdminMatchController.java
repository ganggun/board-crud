package com.example.predict.match.controller;

import com.example.predict.match.dto.MatchCreateRequest;
import com.example.predict.match.dto.MatchResponse;
import com.example.predict.match.dto.MatchResultRequest;
import com.example.predict.match.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/matches")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "관리자 경기", description = "관리자 전용 경기 생성, 수정, 삭제, 결과 등록 API")
public class AdminMatchController {

    private final MatchService matchService;

    public AdminMatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping
    @Operation(
            summary = "경기 생성",
            description = """
                    관리자가 새 경기를 등록합니다.
                    기본 상태를 생략하면 PREDICTING으로 생성되어 경기 시작 전까지 학생들이 예측할 수 있습니다.
                    """
    )
    public MatchResponse create(@Valid @RequestBody MatchCreateRequest request) {
        return matchService.create(request);
    }

    @PutMapping("/{matchId}")
    @Operation(
            summary = "경기 수정",
            description = """
                    경기 이름, 종목, 팀 이름, 시작 시간, 상태를 수정합니다.
                    이미 학생 예측이 있는 경기의 팀 의미를 바꾸면 정산 혼란이 생길 수 있으므로 운영 중에는 주의해야 합니다.
                    """
    )
    public MatchResponse update(
            @Parameter(description = "수정할 경기 ID", example = "1")
            @PathVariable Long matchId,
            @Valid @RequestBody MatchCreateRequest request
    ) {
        return matchService.update(matchId, request);
    }

    @DeleteMapping("/{matchId}")
    @Operation(
            summary = "경기 삭제",
            description = """
                    경기를 삭제합니다.
                    예측 데이터가 연결된 경기는 DB 제약이나 운영 정책에 따라 삭제가 실패할 수 있습니다.
                    실제 운영에서는 삭제보다 CANCELED 상태 변경을 권장합니다.
                    """
    )
    public void delete(
            @Parameter(description = "삭제할 경기 ID", example = "1")
            @PathVariable Long matchId
    ) {
        matchService.delete(matchId);
    }

    @PostMapping("/{matchId}/result")
    @Operation(
            summary = "경기 결과 등록 및 포인트 정산",
            description = """
                    실제 승리 팀을 등록하고 해당 경기의 모든 예측을 정산합니다.
                    정답 예측자는 예측 시점에 고정된 배당률 기준으로 betPoint * odds 만큼 포인트를 받습니다.
                    이미 결과가 등록된 경기는 중복 정산을 막기 위해 다시 처리할 수 없습니다.
                    """
    )
    public MatchResponse finish(
            @Parameter(description = "결과를 등록할 경기 ID", example = "1")
            @PathVariable Long matchId,
            @Valid @RequestBody MatchResultRequest request
    ) {
        return matchService.finish(matchId, request);
    }
}
