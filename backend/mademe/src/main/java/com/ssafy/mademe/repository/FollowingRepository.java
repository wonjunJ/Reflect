package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.Following;
import com.ssafy.mademe.entity.MBTILog;
import com.ssafy.mademe.entity.embedded.EmdFollowing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowingRepository extends JpaRepository<Following, EmdFollowing>, FollowingRepositoryCustom {

}
