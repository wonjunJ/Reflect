package com.ssafy.mademe.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.mademe.dto.response.InterestResponse;
import com.ssafy.mademe.dto.response.RoomDetailResponse;
import com.ssafy.mademe.dto.response.RoomListResponse;
import com.ssafy.mademe.entity.*;
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
import static com.ssafy.mademe.entity.QRoomModeList.roomModeList;

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
                        room.thumbnail,
                        room.title,
                        room.intro,
                        room.question.id
                )
                .from(room)
                .leftJoin(room.hashTags, hashTag)     // HashTag 조인을 추가
                .leftJoin(hashTag.id.hashCode, hashCode)  // HashCode 조인을 추가
                .where(
                        room.totalNum.between(from, to),
                        room.id.notIn(
                                JPAExpressions
                                        .select(roomModeList.id.room.id)
                                        .from(roomModeList)
                                        .where(roomModeList.id.roomMode.eq(RoomMode.SECRET))
                                        .groupBy(roomModeList.id.room.id)
                        ),
                        conditionFiltering(filter),
                        searchWords(searchWord)
                )
                .groupBy(room.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(this::mapToRoomList)
                .collect(Collectors.toList());

        //System.out.println("크기는 " + data.size());
        JPAQuery<Long> count = jpaQueryFactory
                .select(
                        room.countDistinct()
                )
                .from(room)
                .leftJoin(room.hashTags, hashTag)     // HashTag 조인을 추가
                .leftJoin(hashTag.id.hashCode, hashCode)  // HashCode 조인을 추가
                .where(
                        room.totalNum.between(from, to),
                        room.id.notIn(
                                JPAExpressions
                                        .select(roomModeList.id.room.id)
                                        .from(roomModeList)
                                        .where(roomModeList.id.roomMode.eq(RoomMode.SECRET))
                                        .groupBy(roomModeList.id.room.id)
                        ),
                        conditionFiltering(filter),
                        searchWords(searchWord)
                );

        //System.out.println("카운트는 " + count.fetchOne());

        return PageableExecutionUtils.getPage(data, pageable, count::fetchOne);
    }

    @Override
    public List<RoomDetailResponse> findRecommendedRoom(List<InterestResponse> myInterests) {
        return jpaQueryFactory
                .select(
                        room.id,
                        room.type,
                        room.title,
                        room.intro,
                        room.thumbnail,
                        room.question.id
                )
                .from(room)
                .leftJoin(room.hashTags, hashTag)     // HashTag 조인을 추가
                .leftJoin(hashTag.id.hashCode, hashCode)  // HashCode 조인을 추가
                .where(
                        conditionHashName(myInterests)
                )
                .groupBy(room.id)
                .fetch()
                .stream()
                .map(this::mapToRoomDetail)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomDetailResponse> findMoreRecommend(List<Long> ids, int limit) {
        return jpaQueryFactory
                .select(
                        room.id,
                        room.type,
                        room.title,
                        room.intro,
                        room.thumbnail,
                        room.question.id
                )
                .from(room)
                .where(
                        room.id.notIn(ids)
                )
                .limit(limit)
                .fetch()
                .stream()
                .map(this::mapToRoomDetail)
                .collect(Collectors.toList());
    }

    private BooleanExpression conditionHashName(List<InterestResponse> myInterests){
        //return null;
        if(myInterests.isEmpty()){
            System.out.println("비였다.");
            return null;
        }
        BooleanExpression expression = null;
        for(InterestResponse ir : myInterests){
            expression = hashCode.hashName.contains(ir.getInterestName());
            if (expression != null) {
                return expression;
//                if (expression != null) {
//                    expression = expression.or(filterExpression);
//                } else {
//                    expression = filterExpression;
//                }
            }
        }
        return conditionIntro(myInterests);
    }

    private BooleanExpression conditionIntro(List<InterestResponse> myInterests){
        if(myInterests.isEmpty()){
            System.out.println("비였다.");
            return null;
        }
        BooleanExpression expression = null;
        for(InterestResponse ir : myInterests){
            expression = room.intro.contains(ir.getInterestName());
            if (expression != null) {
                return expression;
            }
        }
        return expression;
    }

    private BooleanExpression conditionFiltering(List<String> filter){
        if(filter.isEmpty()){
            System.out.println("비였다.");
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
                .thumnail(tuple.get(5, String.class))
                .title(tuple.get(6, String.class))
                .intro(tuple.get(7, String.class))
                .qId(tuple.get(8, Long.class))
                .build();
    }

    private RoomDetailResponse mapToRoomDetail(Tuple tuple){
        return RoomDetailResponse.builder()
                .roomId(tuple.get(0, Long.class))
                .type(tuple.get(1, RoomType.class))
                .title(tuple.get(2, String.class))
                .intro(tuple.get(3, String.class))
                .thumbnail(tuple.get(4, String.class))
                .qId(tuple.get(5, Long.class))
                .build();
    }
}
