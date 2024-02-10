package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.EmotionCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmotionCodeRepository extends JpaRepository<EmotionCode, Long> {
    EmotionCode findFirstByEmotionName(String emoName);
}
