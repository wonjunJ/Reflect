package com.ssafy.mademe.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.mademe.dto.response.RoomListResponse;
import com.ssafy.mademe.entity.RoomMode;
import com.ssafy.mademe.entity.RoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ssafy.mademe.entity.QHashTag.hashTag;
import static com.ssafy.mademe.entity.QHashCode.hashCode;
import static com.ssafy.mademe.entity.QRoom.room;

@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<RoomListResponse> findTotalRoomList(List<String> filter, String searchWord, int from, int to, Pageable pageable) {
        List<RoomListResponse> data = jpaQueryFactory
                .select(
                        room.id,
                        room.createDate,
                        room.totalNum,
                        room.curNum,
                        room.type,
                        room.mode,
                        room.thumbnail,
                        room.title,
                        room.intro,
                        room.question.id
                )
                .from(room)
                .leftJoin(room.hashTags, hashTag)     // HashTag 조인을 추가
                .fetchJoin()
                .leftJoin(hashTag.id.hashCode, hashCode)  // HashCode 조인을 추가
                .where(
                        room.totalNum.between(from, to),
                        room.mode.ne(RoomMode.SECRET),
                        conditionFiltering(filter),
                        searchWords(searchWord)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(this::mapToRoomList)
                .toList();

        JPAQuery<Long> count = jpaQueryFactory
                .select(
                        room.count()
                )
                .from(room)
                .leftJoin(room.hashTags, hashTag)     // HashTag 조인을 추가
                .fetchJoin()
                .leftJoin(hashTag.id.hashCode, hashCode)  // HashCode 조인을 추가
                .where(
                        room.totalNum.between(from, to),
                        room.mode.ne(RoomMode.SECRET),
                        conditionFiltering(filter),
                        searchWords(searchWord)
                );

        return PageableExecutionUtils.getPage(data, pageable, count::fetchOne);
    }

    private BooleanExpression conditionFiltering(List<String> filter){
        if(filter.isEmpty()){
            return null;
        }
        BooleanExpression expression = null;
        for(String filterItem : filter){ //리스트에 대한 중복 조건 처리를 하는 방법
            BooleanExpression filterExpression = null;
            if(filterItem.equals("FREE") || filterItem.equals("BACKMOON")){
                filterExpression = room.type.eq(RoomType.valueOf(filterItem));
            }else{
                filterExpression = room.totalNum.ne(room.curNum);
            }
            if (expression != null) {
                expression = expression.and(filterExpression);
            } else {
                expression = filterExpression;
            }
        }
        return expression;
    }

    private BooleanExpression searchWords(String word){
        if(word == null){
            return null;
        }
        return room.title.contains(word).or(hashCode.hashName.eq(word)); // 제목 또는 hashName 검색
    }

    private RoomListResponse mapToRoomList(Tuple tuple){
        return RoomListResponse.builder()
                .roomId(tuple.get(0, Long.class))
                .createDate(Objects.requireNonNull(tuple.get(1, LocalDateTime.class)).toLocalDate())
                .totalNum(tuple.get(2, Integer.class))
                .curNum(tuple.get(3, Integer.class))
                .type(tuple.get(4, RoomType.class))
                .mode(tuple.get(5, RoomMode.class))
                .thumnail(tuple.get(6, String.class))
                .title(tuple.get(7, String.class))
                .intro(tuple.get(8, String.class))
                .qId(tuple.get(9, Long.class))
                .build();
    }
}
