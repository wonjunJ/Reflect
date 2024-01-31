package com.ssafy.mademe.service;

import com.ssafy.mademe.dto.response.EmotionCntResponse;
import com.ssafy.mademe.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmotionService {

    public EmotionCntResponse getEmoCnt(User user, int duration){
        LocalDateTime start = LocalDateTime.now().minusDays(duration);
        return new EmotionCntResponse();
    }
}
