package com.ssafy.mademe.controller;

import com.ssafy.mademe.dto.response.FollowingResponse;
import com.ssafy.mademe.dto.response.MonthDiaryResponse;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.service.DiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;

    @GetMapping("/")
    public List<MonthDiaryResponse> getMonthDiaryStatus(@AuthenticationPrincipal User user){
        return diaryService.getMonthDiaryStatus(user);
    }
}
