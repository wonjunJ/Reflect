package com.ssafy.mademe.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.mademe.entity.MBTICode;
import com.ssafy.mademe.entity.MBTILog;
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
                        // E, I
                        mBTILog.mbtiCode.eq(MBTICode.E).count().desc(),
                        //q.count(q.mbtiCode.eq(MBTICode.E)).desc(),
                        mBTILog.mbtiCode.eq(MBTICode.I).count().desc(),
                        //q.count(q.mbtiLog.mbtiCode.eq(MBTICode.I)).desc(),
                        // S, N
                        //q.count(q.mbtiLog.mbtiCode.eq(MBTICode.S)).desc(),
                        mBTILog.mbtiCode.eq(MBTICode.S).count().desc(),
                        //q.count(q.mbtiLog.mbtiCode.eq(MBTICode.N)).desc(),
                        mBTILog.mbtiCode.eq(MBTICode.N).count().desc(),
                        // T, F
                        //q.count(q.mbtiLog.mbtiCode.eq(MBTICode.T)).desc(),
                        mBTILog.mbtiCode.eq(MBTICode.T).count().desc(),
                        //q.count(q.mbtiLog.mbtiCode.eq(MBTICode.F)).desc(),
                        mBTILog.mbtiCode.eq(MBTICode.F).count().desc(),
                        // J, P
                        mBTILog.mbtiCode.eq(MBTICode.J).count().desc(),
                        mBTILog.mbtiCode.eq(MBTICode.P).count().desc()
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
