package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.HashCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashCodeRepository extends JpaRepository<HashCode, Long> {
    HashCode findFirstByHashName(String name);
}
