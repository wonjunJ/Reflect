package com.ssafy.mademe.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.mademe.entity.MBTICode;
import com.ssafy.mademe.entity.MBTILog;
import com.ssafy.mademe.entity.QMBTILog;
import com.ssafy.mademe.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

}
