package com.example.predict.point.controller;

import com.example.predict.point.dto.AdminPointAddRequest;
import com.example.predict.point.dto.PointResponse;
import com.example.predict.point.service.PointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/points")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "관리자 포인트", description = "관리자 전용 사용자 포인트 지급 API")
public class AdminPointController {

    private final PointService pointService;

    public AdminPointController(PointService pointService) {
        this.pointService = pointService;
    }

    @PostMapping("/users/{userId}/add")
    @Operation(
            summary = "사용자 포인트 추가 지급",
            description = """
                    관리자가 특정 사용자에게 포인트를 추가 지급합니다.
                    대상 사용자의 포인트 지갑이 아직 없으면 기본 지갑을 생성한 뒤 지급합니다.
                    지급 내역은 ADMIN_ADJUST 타입의 포인트 거래 내역으로 기록됩니다.
                    """
    )
    public PointResponse addPoint(
            @Parameter(description = "포인트를 지급할 사용자 ID", example = "10")
            @PathVariable Long userId,
            @Valid @RequestBody AdminPointAddRequest request
    ) {
        return new PointResponse(pointService.addByAdmin(userId, request.amount()));
    }
}
