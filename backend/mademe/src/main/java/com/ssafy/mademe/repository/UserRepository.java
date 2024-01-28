package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.Following;
import com.ssafy.mademe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    User findBySiteId(String userId);
    Boolean existsBySiteId(String userId);
    Boolean existsByNickName(String nickName);
    Boolean existsByEmail(String email);
    User findByEmail(String email);
    User findFirstByNickName(String nickName);
}
