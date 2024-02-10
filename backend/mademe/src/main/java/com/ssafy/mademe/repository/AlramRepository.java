package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.Alram;
import com.ssafy.mademe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlramRepository extends JpaRepository<Alram, Long> {
    List<Alram> findByUserAndIsReadIsFalse(User user);
}
