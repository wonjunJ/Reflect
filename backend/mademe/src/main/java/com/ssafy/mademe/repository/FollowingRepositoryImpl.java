package com.ssafy.mademe.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.mademe.dto.response.FollowingResponse;
import com.ssafy.mademe.dto.response.UserInfoResponse;
import com.ssafy.mademe.entity.QFollowing;
import com.ssafy.mademe.entity.QUser;
import com.ssafy.mademe.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class FollowingRepositoryImpl implements FollowingRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<FollowingResponse> findFollowingListByUser(User user, Pageable pageable) {
        List<Tuple> results = jpaQueryFactory
                .select(
                        QUser.user.id,
                        QUser.user.profileImg,
                        QUser.user.nickName
                )
                .from(QFollowing.following)
                .join(QFollowing.following.id.followId, QUser.user)
                .where(
                        QFollowing.following.id.userId.eq(user),
                        QUser.user.isWithdrawal.eq(false)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<FollowingResponse> flist = new ArrayList<>();
        for(Tuple tuple : results){
            flist.add(new FollowingResponse(tuple.get(0, Long.class),
                    tuple.get(1, String.class),
                    tuple.get(2, String.class)));
        }

        JPAQuery<Long> count = jpaQueryFactory
                .select(QFollowing.following.count())
                .from(QFollowing.following)
                .join(QFollowing.following.id.followId, QUser.user)
                .where(
                        QFollowing.following.id.userId.eq(user),
                        QUser.user.isWithdrawal.eq(false)
                );

        return PageableExecutionUtils.getPage(flist, pageable, count::fetchOne);
    }

    @Override
    public UserInfoResponse findFollowingInfoByUser(User user, Long id) {
//        Tuple result = jpaQueryFactory
//                .select(
//
//                )
        return null;
    }
}
