package com.ssafy.mademe.repository;

import com.ssafy.mademe.dto.response.FollowingResponse;
import com.ssafy.mademe.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FollowingRepositoryCustom {
    List<FollowingResponse> findFollowingListByUser(User user, Pageable pageable);
}
