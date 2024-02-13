package com.ssafy.mademe.service;

import com.ssafy.mademe.dto.response.DiaryDetailResponse;
import com.ssafy.mademe.entity.Diary;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryDetailService {
    private final DiaryRepository diaryRepository;

//    public List<Diary> getDiaryDetailByDate(User user, LocalDate date) {
//        LocalDateTime startOfDay = date.atStartOfDay();
//        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay().minusSeconds(1);
//
//        // 해당 날짜 이전에 작성된 3개의 일기, 해당 날짜에 작성된 일기, 해당 날짜 이후에 작성된 3개의 일기 조회
//        return diaryRepository.findDiaryDetailByDate(user, startOfDay, endOfDay);
//    }

    public List<DiaryDetailResponse> findDiaryDetails(User user, Long diaryId) {
       return diaryRepository.findDiaryDetailByDairyId(user, diaryId);
    }
}
