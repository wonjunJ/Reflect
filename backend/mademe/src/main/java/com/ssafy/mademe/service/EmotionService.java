package com.ssafy.mademe.service;

import com.ssafy.mademe.dto.response.EmotionCntResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmotionService {

    public EmotionCntResponse getEmoCnt(int duration){
        return new EmotionCntResponse();
    }
}
