package com.ssafy.mademe.repository;

import com.ssafy.mademe.dto.response.*;
import com.ssafy.mademe.entity.Diary;
import com.ssafy.mademe.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public interface DiaryRepositoryCustom {
    EmotionCntResponse findDiaryEmoCntByUser(User user, LocalDateTime dateTime);
    List<MonthDiaryResponse> findMonthStatus(User user, LocalDate date);
    //SimpleDiaryResponse findSimpleDiaryInfoById(Long id);
    // 일기 디테일페이지 전후 7개 일기 조회
    List<DiaryDetailResponse> findDiaryDetailByDairyId(User user, Long id);

    List<DiaryDetailResponse> findAllDiaryDetailByDairyId(User user, Long id);


    List<GraphResponse> findGraphInfo(User user, int recent);
}
