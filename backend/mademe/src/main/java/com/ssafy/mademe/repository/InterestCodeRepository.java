package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.InterestCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestCodeRepository extends JpaRepository<InterestCode, Long> {
    InterestCode findFirstByHobbyName(String name);
}
