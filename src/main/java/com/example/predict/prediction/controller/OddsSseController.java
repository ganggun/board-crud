package com.example.predict.prediction.controller;

import com.example.predict.prediction.service.OddsSseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
@Tag(name = "실시간", description = "배당률 변경 알림을 받는 SSE API")
public class OddsSseController {

    private final OddsSseService oddsSseService;

    public OddsSseController(OddsSseService oddsSseService) {
        this.oddsSseService = oddsSseService;
    }

    @GetMapping("/odds")
    @Operation(
            summary = "배당률 변경 SSE 구독",
            description = """
                    실시간 배당률 변경 이벤트를 구독합니다.
                    사용자가 예측에 참여해 팀별 베팅 총합이 바뀌면 odds-changed 이벤트가 전송됩니다.
                    프론트는 EventSource로 연결해서 경기별 최신 배당률을 화면에 반영하면 됩니다.
                    """
    )
    public SseEmitter subscribeOdds() {
        return oddsSseService.subscribe();
    }
}
