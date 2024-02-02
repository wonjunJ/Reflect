package com.ssafy.mademe.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.mademe.dto.response.RoomInfoResponse;
import com.ssafy.mademe.entity.RoomType;
import com.ssafy.mademe.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ssafy.mademe.entity.QMyRoom.myRoom;
import static com.ssafy.mademe.entity.QRoom.room;

@RequiredArgsConstructor
public class MyRoomRepositoryImpl implements MyRoomRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RoomInfoResponse> findRoomInfoByUser(User user, LocalDate indate, Boolean isExit, Pageable pageable) {
        return jpaQueryFactory
                .select(
                        room.id,
                        myRoom.bmark,
                        myRoom.inDate,
                        room.createDate,
                        room.type,
                        room.title,
                        room.question.id,
                        myRoom.isExit
                )
                .from(myRoom)
                .join(myRoom.id.room, room)
                .where(
                        myRoom.id.user.eq(user),
                        eqDate(indate),
                        isFalseExit(isExit)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(this::mapToRoomInfoResponse)
                .collect(Collectors.toList());
    }

    private BooleanExpression eqDate(LocalDate indate){ //이 메소드가 null을 리턴하면 위의 쿼리 조건문 무시
        if(indate == null){ //날짜 조건이 없다.
            return null;
        }
        return myRoom.inDate.between(indate.atStartOfDay(), indate.atTime(LocalTime.MAX));
    }

    private BooleanExpression isFalseExit(Boolean isExit){ //isExit가 false면 퇴장하지 않은 방만 보내주도록 하기 위해
        if(isExit == null){
            return null;
        }
        return myRoom.isExit.eq(isExit);
    }

    private RoomInfoResponse mapToRoomInfoResponse(Tuple tuple){
        return RoomInfoResponse.builder()
                .roomId(tuple.get(0, Long.class))
                .bmark(Boolean.TRUE.equals(tuple.get(1, Boolean.class)))
                .inDate(Objects.requireNonNull(tuple.get(2, LocalDateTime.class)).toLocalDate())
                .createDate(Objects.requireNonNull(tuple.get(3, LocalDateTime.class)).toLocalDate())
                .type(tuple.get(4, RoomType.class))
                .title(tuple.get(5, String.class))
                .qId(tuple.get(6, Long.class))
                .isExit(Boolean.TRUE.equals(tuple.get(7, Boolean.class)))
                .build();
    }
}
