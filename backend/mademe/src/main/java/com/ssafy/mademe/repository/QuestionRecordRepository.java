package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.QuestionRecord;
import com.ssafy.mademe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRecordRepository extends JpaRepository<QuestionRecord, Long> {
    List<QuestionRecord> findByCompositeKey_User(User user);
}
