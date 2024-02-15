package com.ssafy.mademe.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.mademe.dto.response.*;
//import com.ssafy.mademe.entity.QDiary;
import com.ssafy.mademe.entity.Diary;
import com.ssafy.mademe.entity.EmotionCode;
import com.ssafy.mademe.entity.QDiary;
import com.ssafy.mademe.entity.User;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
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



    @Override
    public List<DiaryDetailResponse> findAllDiaryDetailByDairyId(User user, Long id) {
        List<Diary> AllDiaries = jpaQueryFactory
                .selectFrom(diary)
                .fetch();

        return AllDiaries
                .stream()
                .map(this::mapToDiaryDetail)
                .collect(Collectors.toList());
    }

    @Override
    public List<DiaryDetailResponse> findDiaryDetailByDairyId(User user, Long id) {
        Long diaryId = id;
        QDiary diary = QDiary.diary;

        // 조회된 일기의 생성 날짜 조회
        LocalDateTime targetCreateDate = jpaQueryFactory
                .select(diary.createDate)
                .from(diary)
                .where(diary.id.eq(diaryId))
                .fetchOne();

        // 현재 일기 조회
        Diary currentDiary = jpaQueryFactory
                .selectFrom(diary)
                .where(diary.id.eq(diaryId)
                        .and(diary.user.eq(user)))
                .fetchOne();

        // 전의 데이터 조회
        List<Diary> previousDiaries = jpaQueryFactory
                .selectFrom(diary)
                .where(diary.createDate.before(targetCreateDate)
                        .and(diary.user.eq(user)))
                .orderBy(diary.createDate.desc())
                .limit(3)
                .fetch();
        Collections.reverse(previousDiaries);

        // 후의 데이터 조회
        List<Diary> nextDiaries = jpaQueryFactory
                .selectFrom(diary)
                .where(diary.createDate.after(targetCreateDate)
                        .and(diary.user.eq(user)))
                .orderBy(diary.createDate.asc()) // 정렬 순서 변경
                .limit(3)
                .fetch();

        // 결과 병합
        List<Diary> combinedDiaries = new ArrayList<>(previousDiaries);
        combinedDiaries.add(currentDiary);
        combinedDiaries.addAll(nextDiaries);

        return combinedDiaries
                .stream()
                .map(this::mapToDiaryDetail)
                .collect(Collectors.toList());

    }


    @Override
    public List<GraphResponse> findGraphInfo(User user, int recent) {
        return jpaQueryFactory
                .select(
                        diary.createDate,
                        diary.diaryScore
                )
                .from(diary)
                .where(
                        diary.user.eq(user)
                )
                .orderBy(
                        diary.createDate.desc()
                )
                .limit(recent)
                .fetch()
                .stream()
                .map(this::mapToGraphResponse)
                .collect(Collectors.toList());
        //return null;

    }

    private MonthDiaryResponse mapToMonthDiaryResponse(Tuple tuple){
        return MonthDiaryResponse.builder()
                .diaryId(tuple.get(0, Long.class))
                .date(Objects.requireNonNull(tuple.get(1, LocalDateTime.class)).toLocalDate())
                .emotion(tuple.get(2, Long.class))
                .build();
    }

    private GraphResponse mapToGraphResponse(Tuple tuple){
        return GraphResponse.builder()
                .date(Objects.requireNonNull(tuple.get(0, LocalDateTime.class)).toLocalDate())
                .score(tuple.get(1, Integer.class))
                .build();
    }

    private DiaryDetailResponse mapToDiaryDetail(Diary diary){
        return DiaryDetailResponse.builder()
                .diaryId(diary.getId())
                .createDate(diary.getCreateDate().toLocalDate())
                .content(diary.getContent())
                .diaryImg(diary.getDiaryImg())
                .diaryScore(diary.getDiaryScore())
                .keyword(diary.getKeyword())
                .video(diary.getVideo())
                .build();
    }
}
