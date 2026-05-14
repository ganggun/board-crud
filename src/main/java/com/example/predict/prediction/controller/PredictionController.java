package com.example.predict.prediction.controller;

import com.example.predict.auth.security.CurrentUser;
import com.example.predict.auth.security.LoginUser;
import com.example.predict.prediction.dto.PredictionCreateRequest;
import com.example.predict.prediction.dto.PredictionResponse;
import com.example.predict.prediction.service.PredictionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "승부예측", description = "학생의 경기 예측 참여와 내 예측 조회 API")
public class PredictionController {

    private final PredictionService predictionService;

    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @PostMapping("/matches/{matchId}/predictions")
    @Operation(
            summary = "경기 승부예측 참여",
            description = """
                    로그인한 학생이 특정 경기의 승리 팀을 선택하고 포인트를 겁니다.
                    경기 상태가 PREDICTING이고 시작 시간 전일 때만 참여할 수 있습니다.
                    한 사용자는 한 경기당 한 번만 예측할 수 있으며, 참여 즉시 포인트가 차감됩니다.
                    배당률은 예측 시점의 실시간 배당률로 고정되어 저장됩니다.
                    """
    )
    public PredictionResponse create(
            @Parameter(description = "예측할 경기 ID", example = "1")
            @PathVariable Long matchId,
            @Parameter(hidden = true) @CurrentUser LoginUser loginUser,
            @Valid @RequestBody PredictionCreateRequest request
    ) {
        return predictionService.create(matchId, loginUser, request);
    }

    @GetMapping("/predictions/me")
    @Operation(
            summary = "내 예측 목록 조회",
            description = """
                    로그인한 사용자가 참여한 예측 목록을 최신순으로 조회합니다.
                    각 예측에는 선택 팀, 건 포인트, 고정 배당률, 예상 보상, 정산 결과가 포함됩니다.
                    """
    )
    public List<PredictionResponse> myPredictions(@Parameter(hidden = true) @CurrentUser LoginUser loginUser) {
        return predictionService.listMyPredictions(loginUser);
    }
}
