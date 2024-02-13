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

    public List<DiaryDetailResponse> findAllDiaryDetails(User user, Long diaryId) {
        return diaryRepository.findAllDiaryDetailByDairyId(user, diaryId);
    }

    public List<DiaryDetailResponse> findDiaryDetails(User user, Long diaryId) {
        return diaryRepository.findDiaryDetailByDairyId(user, diaryId);
    }
}
