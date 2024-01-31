package com.ssafy.mademe.service;

import com.ssafy.mademe.dto.response.MonthDiaryResponse;
import com.ssafy.mademe.entity.Diary;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.repository.DiaryRepository;
import com.ssafy.mademe.repository.MyRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final MyRoomRepository myRoomRepository;

    public List<MonthDiaryResponse> getMonthDiaryStatus(User user){
        Month now = LocalDateTime.now().getMonth();
        System.out.println(now);
        List<Diary> monthInfo = diaryRepository.findMonthStatus(user, now);
        List<MonthDiaryResponse> result = new ArrayList<>();
        for(Diary d : monthInfo){
            LocalDate date = d.getCreateDate().toLocalDate();
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.atTime(LocalTime.MAX);
            result.add(
                    MonthDiaryResponse.builder()
                    .diaryId(d.getId())
                    .date(d.getCreateDate().toLocalDate())
                    .emotion(d.getTotalEmotion().getId())
                    .doCommunity(myRoomRepository.existsById_UserAndInDateBetween(user, start, end))
                    .build()
            );
        }
        return result;
    }
}
