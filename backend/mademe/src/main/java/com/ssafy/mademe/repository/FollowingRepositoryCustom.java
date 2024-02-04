package com.ssafy.mademe.repository;

import com.ssafy.mademe.dto.response.FollowingResponse;
import com.ssafy.mademe.dto.response.UserInfoResponse;
import com.ssafy.mademe.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FollowingRepositoryCustom {
    Page<FollowingResponse> findFollowingListByUser(User user, Pageable pageable);
    UserInfoResponse findFollowingInfoByUser(User user, Long id);
}
