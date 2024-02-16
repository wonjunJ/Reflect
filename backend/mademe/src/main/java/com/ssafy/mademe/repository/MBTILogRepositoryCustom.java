package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.MBTICode;
import com.ssafy.mademe.entity.User;

import java.util.List;
import java.util.Map;

public interface MBTILogRepositoryCustom {
    Map<MBTICode, Long> findMbtiByUser(User user);
}
