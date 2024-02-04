package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.Following;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.entity.embedded.EmdFollowing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowingRepository extends JpaRepository<Following, EmdFollowing>, FollowingRepositoryCustom {
    //팔로우하는 사용자인지 아닌지를 확인한다.
    Boolean existsById_UserIdAndId_FollowId(User user, User follow);
}
