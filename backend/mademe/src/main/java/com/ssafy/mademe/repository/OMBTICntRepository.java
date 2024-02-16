package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.OMBTICnt;
import com.ssafy.mademe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OMBTICntRepository extends JpaRepository<OMBTICnt, Long> {
    List<OMBTICnt> findByCompositeKey_User(User user);
}
