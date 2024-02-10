package com.ssafy.mademe.service;

import com.ssafy.mademe.dto.response.DiaryLogResponse;
import com.ssafy.mademe.dto.response.EmotionCntResponse;
import com.ssafy.mademe.dto.response.GraphResponse;
import com.ssafy.mademe.dto.response.PagingInfoResponse;
import com.ssafy.mademe.entity.EmotionLog;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.repository.DiaryRepository;
import com.ssafy.mademe.repository.EmotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmotionService {
    private final DiaryRepository diaryRepository;
    private final EmotionRepository emotionRepository;

    public EmotionCntResponse getEmoCnt(User user, int duration){
        LocalDateTime start = LocalDate.now().minusDays(duration).atStartOfDay();
        return diaryRepository.findDiaryEmoCntByUser(user, start);
    }

    public Map<String, Object> getEmoLogInfos(User user, Long code, Pageable pageable){
        Page<EmotionLog> emotionLogs = emotionRepository.findByDiaryUserAndEmotionCodeIdOrderByDiaryCreateDateDesc(user, code, pageable);
        List<DiaryLogResponse> logdata = new ArrayList<>();
        for(EmotionLog logs : emotionLogs){
            logdata.add(mapToDiaryLog(logs));
        }
        Map<String, Object> json = new HashMap<>();
        //페이징 처리를 위한 Page객체로 변환
        Page<DiaryLogResponse> result = new PageImpl<>(logdata, pageable, emotionLogs.getTotalElements());
        json.put("pagingInfo", PagingInfoResponse.builder()
                .totalPage(result.getTotalPages()) //전체 페이지 수
                .pageNum(result.getNumber()) //현재 페이지 번호
                .isFirst(result.isFirst()) //이 페이지가 첫번째 페이지 인지
                .hasNext(result.hasNext()) //다음 페이지가 존재하는지
                .build()
        );
        json.put("data", logdata);
        return json;
    }

    public List<GraphResponse> getEmoGraphInfo(User user, int recent){
        return diaryRepository.findGraphInfo(user, recent);
    }

    private DiaryLogResponse mapToDiaryLog(EmotionLog emotionLog){
        return DiaryLogResponse.builder()
                .id(emotionLog.getId())
                .diaryId(emotionLog.getDiary().getId())
                .createDate(emotionLog.getDiary().getCreateDate())
                .sentence(emotionLog.getSentence())
                .build();
    }
}
