package com.ssafy.mademe.service;

import com.ssafy.mademe.dto.response.EmotionCntResponse;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class EmotionService {
    private final DiaryRepository diaryRepository;

    public EmotionCntResponse getEmoCnt(User user, int duration){
        LocalDateTime start = LocalDate.now().minusDays(7).atTime(LocalTime.MIN);
        return diaryRepository.findDiaryEmoCntByUser(user, start);
    }
}
