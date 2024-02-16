package com.ssafy.mademe.controller;

import com.ssafy.mademe.dto.response.EmotionCntResponse;
import com.ssafy.mademe.dto.response.GraphResponse;
import com.ssafy.mademe.entity.MBTICode;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.service.EmotionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/emotion")
@RequiredArgsConstructor
public class EmotionController {

    private final EmotionService emotionService;

    //기간별 일기 총괄 감정의 갯수를 조회
    @GetMapping("/")
    public Map<String, EmotionCntResponse> getRecentEmoCnt(@AuthenticationPrincipal User user){
        //7일, 14일, 1달치 값들을 한꺼번에 보내줄까 따로 보내줄까
        Map<String, EmotionCntResponse> json = new HashMap<>();
        json.put("7days", emotionService.getEmoCnt(user, 7));
        json.put("14days", emotionService.getEmoCnt(user, 14));
        json.put("1month", emotionService.getEmoCnt(user, 30));
        return json;
    }

    //감정 일기 분석 정보
    @GetMapping("/{code}")
    public Map<String, Object> getEmoLogInfo(@AuthenticationPrincipal User user, @PathVariable("code") Long code,
                                             @PageableDefault(size = 5) Pageable pageable){ //?page=00 담아서 보내주기
        return emotionService.getEmoLogInfos(user, code, pageable);
    }

    @GetMapping("/graph")
    public List<GraphResponse> getEmoGraphInfo(@AuthenticationPrincipal User user, @RequestParam("recent") int recent){
        return emotionService.getEmoGraphInfo(user, recent);
    }
}
