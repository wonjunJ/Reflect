package com.ssafy.mademe.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.mademe.dto.response.FollowingResponse;
import com.ssafy.mademe.dto.response.InterestResponse;
import com.ssafy.mademe.dto.response.MyRoomInfoResponse;
import com.ssafy.mademe.entity.Room;
import com.ssafy.mademe.entity.RoomMode;
import com.ssafy.mademe.entity.RoomType;
import com.ssafy.mademe.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ssafy.mademe.entity.QMyRoom.myRoom;
import static com.ssafy.mademe.entity.QRoom.room;
import static com.ssafy.mademe.entity.QUser.user;

@RequiredArgsConstructor
public class MyRoomRepositoryImpl implements MyRoomRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final RedisTemplate<Object, Object> redisTemplate;

    @Override
    public Page<MyRoomInfoResponse> findRoomInfoListByUser(User user, LocalDate indate, Boolean isExit, Pageable pageable) {
        List<MyRoomInfoResponse> data = jpaQueryFactory
                .select(
                        room.id,
                        myRoom.bmark,
                        myRoom.inDate,
                        room.createDate,
                        room.totalNum,
                        room.curNum,
                        room.type,
                        room.thumbnail,
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
                .toList();

        JPAQuery<Long> count = jpaQueryFactory
                .select(myRoom.count())
                .from(myRoom)
                .join(myRoom.id.room, room)
                .where(
                        myRoom.id.user.eq(user),
                        eqDate(indate),
                        isFalseExit(isExit)
                );

        //QueryDsl에서 페이징 처리 최적화를 하기 위해
        return PageableExecutionUtils.getPage(data, pageable, count::fetchOne);
    }

    @Override
    public List<FollowingResponse> findParticipantsByRoom(Room roomId) {
        return jpaQueryFactory
                .select(
                        user.id,
                        user.profileImg,
                        user.nickName
                )
                .from(myRoom)
                .join(myRoom.id.user, user)
                .where(
                        myRoom.id.room.eq(roomId),
                        myRoom.isExit.eq(false),
                        user.isWithdrawal.eq(false)
                )
                .fetch()
                .stream()
                .map(this::mapToFollowingInfo)
                .collect(Collectors.toList());
    }

    private BooleanExpression eqDate(LocalDate indate){ //이 메소드가 null을 리턴하면 위의 쿼리 조건문 무시
        if(indate == null){ //날짜 조건이 없다.
            return null;
        }
        return myRoom.inDate.between(indate.atStartOfDay(), indate.atTime(LocalTime.MAX));
    }

    private BooleanExpression isFalseExit(Boolean isExit){ //isExit가 false면 퇴장하지 않은 방만 조회하기 위해
        if(isExit == null){
            return null;
        }
        return myRoom.isExit.eq(isExit);
    }

    private MyRoomInfoResponse mapToRoomInfoResponse(Tuple tuple){
        return MyRoomInfoResponse.builder()
                .roomId(tuple.get(0, Long.class))
                .bmark(Boolean.TRUE.equals(tuple.get(1, Boolean.class)))
                .inDate(Objects.requireNonNull(tuple.get(2, LocalDateTime.class)).toLocalDate())
                .createDate(Objects.requireNonNull(tuple.get(3, LocalDateTime.class)).toLocalDate())
                .totalNum(tuple.get(4, Integer.class))
                .curNum(tuple.get(5, Integer.class))
                .type(tuple.get(6, RoomType.class))
                .thumnail(tuple.get(7, String.class))
                .title(tuple.get(8, String.class))
                .qId(tuple.get(9, Long.class)) //null이 될수도 있다.
                .isExit(Boolean.TRUE.equals(tuple.get(10, Boolean.class)))
                .build();
    }

    private FollowingResponse mapToFollowingInfo(Tuple tuple){
        Long pid = tuple.get(0, Long.class); //이 참가자의 아이디값
        return FollowingResponse.builder()
                .userId(pid)
                .profileImg(tuple.get(1, String.class))
                .nickName(tuple.get(2, String.class))
                .is_online(redisTemplate.opsForValue().get("AT:" + pid) != null)
                .build();
    }
}
