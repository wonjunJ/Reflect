package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.EmotionLog;
import com.ssafy.mademe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmotionRepository extends JpaRepository<EmotionLog, Long> {
}
