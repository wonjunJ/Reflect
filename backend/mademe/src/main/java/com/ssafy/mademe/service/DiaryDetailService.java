package com.ssafy.mademe.service;

import com.ssafy.mademe.dto.response.DiaryDetailResponse;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryDetailService {
    private final DiaryRepository diaryRepository;
    // 모든 일기 조회용
//    public List<DiaryDetailResponse> findAllDiaryDetails(User user, Long diaryId) {
//        return diaryRepository.findAllDiaryDetailByDairyId(user, diaryId);
//    }
    // 7개 일기 조회용
    public List<DiaryDetailResponse> findDiaryDetails(User user, Long diaryId) {
        return diaryRepository.findDiaryDetailByDairyId(user, diaryId);
    }
}
