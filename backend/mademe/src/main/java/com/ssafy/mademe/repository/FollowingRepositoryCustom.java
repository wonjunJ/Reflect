package com.ssafy.mademe.repository;

import com.ssafy.mademe.dto.response.FollowingResponse;
import com.ssafy.mademe.entity.User;

import java.util.List;

public interface FollowingRepositoryCustom {
    List<FollowingResponse> findFollowingListByUser(User user, int pgNum);
}
