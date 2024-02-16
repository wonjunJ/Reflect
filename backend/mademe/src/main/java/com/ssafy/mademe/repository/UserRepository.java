package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.Following;
import com.ssafy.mademe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    User findBySiteId(String userId);
    Boolean existsBySiteId(String userId);
    Boolean existsByNickName(String nickName);
    Boolean existsByEmail(String email);
    User findByEmail(String email);
    User findFirstByNickName(String nickName);
    Boolean existsBySiteIdAndIsOauthIsTrue(String userId);
    @Modifying
    @Query("UPDATE User e SET e.recordTime = e.recordTime + 1 WHERE e.id = :id")
    void increaseCntById(@Param("id") Long id);
}
