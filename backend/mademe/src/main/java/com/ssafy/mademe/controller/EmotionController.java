package com.ssafy.mademe.controller;

import com.ssafy.mademe.dto.response.EmotionCntResponse;
import com.ssafy.mademe.entity.MBTICode;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.service.EmotionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/emotion")
@RequiredArgsConstructor
public class EmotionController {

    private final EmotionService emotionService;

    //기간별 일기 총괄 감정의 갯수를 조회
    @GetMapping("/")
    public EmotionCntResponse getRecentEmoCnt(@AuthenticationPrincipal User user, @RequestParam("duration") int duration){
        //7일, 14일, 1달치 값들을 한꺼번에 보내줄까 따로 보내줄까
        return emotionService.getEmoCnt(user, duration);
    }

    //감정 일기 분석 정보
    @GetMapping("/{code}")
    public Map<String, Object> getEmoLogInfo(@AuthenticationPrincipal User user, @PathVariable("code") Long code,
                                             @PageableDefault(size = 5) Pageable pageable){
        return emotionService.getEmoLogInfos(user, code, pageable);
    }
}
