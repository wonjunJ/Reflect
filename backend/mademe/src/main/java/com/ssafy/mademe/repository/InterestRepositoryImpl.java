package com.ssafy.mademe.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.mademe.dto.response.InterestLsResponse;
import com.ssafy.mademe.dto.response.InterestResponse;
import com.ssafy.mademe.entity.Interest;
import com.ssafy.mademe.entity.Preference;
import com.ssafy.mademe.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.ssafy.mademe.entity.QInterest.interest;

@RequiredArgsConstructor
public class InterestRepositoryImpl implements InterestRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<InterestResponse> findInterestList(User user, int top, Preference prf) {
        JPAQuery<Interest> query = jpaQueryFactory
                .selectFrom(interest)
                .where(interest.user.eq(user));

        if (prf != null) {
            query.where(interest.compositeKey.preference.eq(prf));
        }

        return query
                .orderBy(interest.cnt.desc())
                .limit(top)
                .fetch()
                .stream()
                .map(this::mapToInterestResponse)  // 변환 메서드 호출
                .collect(Collectors.toList());

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

    private InterestResponse mapToInterestResponse(Interest interest) {
        return InterestResponse.builder()
                .interestName(interest.getCompositeKey().getInterestCode().getHobbyName())
                .preference(interest.getCompositeKey().getPreference())
                .cnt(interest.getCnt())
                .build();
    }
}
