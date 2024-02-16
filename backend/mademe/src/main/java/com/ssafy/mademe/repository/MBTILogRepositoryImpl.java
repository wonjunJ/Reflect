package com.ssafy.mademe.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.mademe.entity.MBTICode;
import com.ssafy.mademe.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ssafy.mademe.entity.QMBTILog.mBTILog;

@RequiredArgsConstructor
public class MBTILogRepositoryImpl implements MBTILogRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Map<MBTICode, Long> findMbtiByUser(User user) {
        List<Tuple> results = jpaQueryFactory
                .select(
                        mBTILog.mbtiCode,
                        mBTILog.sentence.count().as("cnt")
                )
                .from(mBTILog)
                .where(mBTILog.user.eq(user))
                .groupBy(mBTILog.mbtiCode)
                .orderBy(
                        mBTILog.sentence.count().desc()
                )
                .fetch();

        //List<Map<MBTICode, Long>> values = new ArrayList<>();
        Map<MBTICode, Long> map = new HashMap<>();
        for(Tuple tuple : results){
            //mbtiLog.setId(tuple.get(0, Long.class));
            map.put(tuple.get(0, MBTICode.class), tuple.get(1, Long.class));
            //mbtiLog.setCnt(tuple.get(1, Long.class));
//            values.add(map);
        }
        return map;
    }
}
