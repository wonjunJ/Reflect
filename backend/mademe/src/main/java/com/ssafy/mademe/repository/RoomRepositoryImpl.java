package com.ssafy.mademe.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.mademe.dto.response.FollowingResponse;
import com.ssafy.mademe.dto.response.InterestResponse;
import com.ssafy.mademe.dto.response.RoomDetailResponse;
import com.ssafy.mademe.dto.response.RoomListResponse;
import com.ssafy.mademe.entity.Room;
import com.ssafy.mademe.entity.RoomMode;
import com.ssafy.mademe.entity.RoomType;
import com.ssafy.mademe.entity.User;
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
                //.leftJoin(room.roomModes, roomModeList)
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
                //.distinct()
                .map(this::mapToRoomList)
                .toList();

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

        //System.out.println(count);

        return PageableExecutionUtils.getPage(data, pageable, count::fetchOne);
    }

    @Override
    public List<RoomDetailResponse> findRecommendedRoom(User user, List<InterestResponse> myInterests) {
        return null;
//        return jpaQueryFactory
//                .select(
//
//                )
//                .from(room)
//                .leftJoin(room.hashTags, hashTag)     // HashTag 조인을 추가
//                .leftJoin(hashTag.id.hashCode, hashCode)  // HashCode 조인을 추가
//                .where(
//                        conditionHashName(myInterests).or(conditionIntro(myInterests))
//                )
//                .fetch();
    }

    private BooleanExpression conditionHashName(List<InterestResponse> myInterests){
        return null;
//        if(myInterests.isEmpty()){
//            System.out.println("비였다.");
//            return null;
//        }
//        BooleanBuilder builder = new BooleanBuilder();
//        for (InterestResponse ir : myInterests) {
//            if (hashCode.hashName.contains(ir.getInterestName())) {
//                builder.or(hashCode.hashName.contains(ir.getInterestName()));
//            }
//        }
//        return builder.getValue();
    }

    private BooleanExpression conditionIntro(List<InterestResponse> myInterests){
        if(myInterests.isEmpty()){
            System.out.println("비였다.");
            return null;
        }
        BooleanExpression expression = null;
        for(InterestResponse ir : myInterests){
            BooleanExpression filterExpression = null;
            filterExpression = room.intro.contains(ir.getInterestName());
            if (expression != null) {
                expression = expression.or(filterExpression);
            } else {
                expression = filterExpression;
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

    private RoomDetailResponse mapToRoomDetail(Room room, List<FollowingResponse> participants,
                                               List<String> hashTags, List<RoomMode> modes){
        return RoomDetailResponse.builder()
                .roomId(room.getId())
                .createDate(room.getCreateDate().toLocalDate())
                .type(room.getType())
                .mode(modes)
                .ownerId(room.getOwnerId().getId())
                .title(room.getTitle())
                .intro(room.getIntro())
                .thumbnail(room.getThumbnail())
                .qId(room.getQuestion() == null ? null : room.getQuestion().getId())
                .participants(participants)
                .hashTags(hashTags)
                .build();
    }
}
