package com.ssafy.mademe.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.mademe.dto.response.InterestLsResponse;
import com.ssafy.mademe.entity.Interest;
import com.ssafy.mademe.entity.Preference;
import com.ssafy.mademe.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ssafy.mademe.entity.QInterest.interest;

@RequiredArgsConstructor
public class InterestRepositoryImpl implements InterestRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Interest> findInterestList(User user, int top, Preference prf) {
        JPAQuery<Interest> query = jpaQueryFactory
                .selectFrom(interest)
                .where(interest.user.eq(user));

        if (prf != null) {
            query.where(interest.compositeKey.preference.eq(prf));
        }

        return query
                .orderBy(interest.cnt.desc())
                .limit(top)
                .fetch();

// --------------------------------------------------------------------
//        return jpaQueryFactory
//                .selectFrom(interest)
//                .where(
//                        interest.user.eq(user),
//                        interest.compositeKey.preference.eq(prf)
//                )
//                .orderBy(interest.cnt.desc())
//                .limit(top)
//                .fetch();
    }
}
