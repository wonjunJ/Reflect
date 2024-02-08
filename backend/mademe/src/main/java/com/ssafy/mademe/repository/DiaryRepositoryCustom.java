package com.ssafy.mademe.repository;

import com.ssafy.mademe.dto.response.EmotionCntResponse;
import com.ssafy.mademe.dto.response.MonthDiaryResponse;
import com.ssafy.mademe.dto.response.SimpleDiaryResponse;
import com.ssafy.mademe.entity.Diary;
import com.ssafy.mademe.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public interface DiaryRepositoryCustom {
    EmotionCntResponse findDiaryEmoCntByUser(User user, LocalDateTime dateTime);
    List<MonthDiaryResponse> findMonthStatus(User user, LocalDate date);
    //SimpleDiaryResponse findSimpleDiaryInfoById(Long id);
<<<<<<< HEAD

    // 일기 디테일페이지 전후 7개 일기 조회
    List<Diary> findDiaryDetailByDate(User user, LocalDateTime startOfDay, LocalDateTime endOfDay);


=======
    List<Integer> findGraphInfo(User user, int recent);
>>>>>>> 7933abd5393eaf52dad61a1aced428aa0cdfe306
}
