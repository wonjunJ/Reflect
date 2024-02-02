package com.ssafy.mademe.repository;

import com.ssafy.mademe.dto.response.EmotionCntResponse;
import com.ssafy.mademe.dto.response.SimpleDiaryResponse;
import com.ssafy.mademe.entity.Diary;
import com.ssafy.mademe.entity.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public interface DiaryRepositoryCustom {
    EmotionCntResponse findDiaryEmoCntByUser(User user, LocalDateTime dateTime);
    List<Diary> findMonthStatus(User user, Month now);
    //SimpleDiaryResponse findSimpleDiaryInfoById(Long id);
}
