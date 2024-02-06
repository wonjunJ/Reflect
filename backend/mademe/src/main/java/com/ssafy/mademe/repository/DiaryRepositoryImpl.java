package com.ssafy.mademe.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.mademe.dto.response.EmotionCntResponse;
//import com.ssafy.mademe.entity.QDiary;
import com.ssafy.mademe.dto.response.MonthDiaryResponse;
import com.ssafy.mademe.dto.response.SimpleDiaryResponse;
import com.ssafy.mademe.entity.Diary;
import com.ssafy.mademe.entity.EmotionCode;
import com.ssafy.mademe.entity.User;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ssafy.mademe.entity.QDiary.diary;

@RequiredArgsConstructor
public class DiaryRepositoryImpl implements DiaryRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public EmotionCntResponse findDiaryEmoCntByUser(User user, LocalDateTime dateTime) {
        List<Tuple> results = jpaQueryFactory
                .select(
                        diary.totalEmotion,
                        diary.id.count().as("cnt")
                )
                .from(diary)
                .where(diary.user.eq(user), diary.createDate.after(dateTime))
                .groupBy(diary.totalEmotion)
                .fetch();

        EmotionCntResponse cnt = new EmotionCntResponse();
//        if(results.isEmpty()){
//            return cnt;
//        }
        for(Tuple tuple : results){
            EmotionCode codeId = tuple.get(0, EmotionCode.class);
            assert codeId != null;
            if(codeId.getId() == 1){
                cnt.setHappiness(tuple.get(1, Long.class));
            } else if (codeId.getId() == 2) {
                cnt.setJoy(tuple.get(1, Long.class));
            } else if (codeId.getId() == 3) {
                cnt.setSad(tuple.get(1, Long.class));
            } else if (codeId.getId() == 4) {
                cnt.setAnger(tuple.get(1, Long.class));
            } else if (codeId.getId() == 5) {
                cnt.setDisgust(tuple.get(1, Long.class));
            } else {
                cnt.setNone(tuple.get(1, Long.class));
            }
        }
        return cnt;
    }

    @Override
    public List<MonthDiaryResponse> findMonthStatus(User user, LocalDate date) {
        return jpaQueryFactory
                .select(
                        diary.id,
                        diary.createDate,
                        diary.totalEmotion.id
                )
                .from(diary)
                .where(
                        diary.user.eq(user),
                        diary.createDate.year().eq(date.getYear()),
                        diary.createDate.month().eq(date.getMonthValue())
                )
                .fetch()
                .stream()
                .map(this::mapToMonthDiaryResponse)
                .collect(Collectors.toList());
    }

    private MonthDiaryResponse mapToMonthDiaryResponse(Tuple tuple){
        return MonthDiaryResponse.builder()
                .diaryId(tuple.get(0, Long.class))
                .date(Objects.requireNonNull(tuple.get(1, LocalDateTime.class)).toLocalDate())
                .emotion(tuple.get(2, Long.class))
                .build();
    }
}
