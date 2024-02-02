package com.ssafy.mademe.service;

import com.ssafy.mademe.entity.QuestionRecord;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.repository.QuestionRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRecordRepository questionRecordRepository;

    public List<QuestionRecord> getMyQRecord(User user){
        return questionRecordRepository.findByCompositeKey_User(user);
    }
}
