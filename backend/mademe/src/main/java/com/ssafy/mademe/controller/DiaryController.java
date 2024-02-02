package com.ssafy.mademe.controller;

import com.ssafy.mademe.dto.response.FollowingResponse;
import com.ssafy.mademe.dto.response.MonthDiaryResponse;
import com.ssafy.mademe.dto.response.SimpleDiaryResponse;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.service.DiaryService;
import com.ssafy.mademe.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;
    private final RoomService roomService;

    @GetMapping("/")
    public List<MonthDiaryResponse> getMonthDiaryStatus(@AuthenticationPrincipal User user){
        return diaryService.getMonthDiaryStatus(user);
    }

    //간단 데일리 정보 얻기
    @GetMapping("/simple")
    public Map<String, Object> getSimpleDailyInfo(@AuthenticationPrincipal User user, @RequestParam("diaryId") Long diaryId,
                                                  Pageable pageable){
        Map<String, Object> json = new HashMap<>();
        SimpleDiaryResponse dInfos = diaryService.getSimpleDiaryInfo(diaryId);
        json.put("diary", dInfos);
        json.put("community", roomService.getRoomInfos(user, dInfos.getDate(), null, pageable));
        return json;
    }
}
