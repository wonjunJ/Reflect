package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findBySiteId(String userId);
    Boolean existsBySiteId(String userId);
    Boolean existsByNickName(String nickName);
}
