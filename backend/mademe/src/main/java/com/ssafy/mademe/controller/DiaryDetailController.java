package com.ssafy.mademe.controller;

import com.ssafy.mademe.dto.response.DiaryDetailResponse;
import com.ssafy.mademe.entity.Diary;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.service.DiaryDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/DairyDetail")
@RequiredArgsConstructor
public class DiaryDetailController {

    private final DiaryDetailService diaryDetailService;

    @GetMapping("/{date}")
    public ResponseEntity<List<DiaryDetailResponse>> getDiaryDetailByDate(
            @AuthenticationPrincipal User user,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<Diary> diaries = diaryDetailService.getDiaryDetailByDate(user, date);
        List<DiaryDetailResponse> diaryDetailResponses = diaries.stream()
                .map(diary -> new DiaryDetailResponse(diary)) // DiaryResponse 생성자는 Diary 객체를 받아 DTO를 생성
                .collect(Collectors.toList());

        return ResponseEntity.ok(diaryDetailResponses);

    }

}
