package com.ssafy.mademe.service;

import com.ssafy.mademe.dto.gpt.request.DiaryGPTRequest;
import com.ssafy.mademe.dto.response.MonthDiaryResponse;
import com.ssafy.mademe.dto.response.SimpleDiaryResponse;
import com.ssafy.mademe.entity.*;
import com.ssafy.mademe.exception.diary.DiaryNotFoundException;
import com.ssafy.mademe.repository.DiaryRepository;
import com.ssafy.mademe.repository.EmotionCodeRepository;
import com.ssafy.mademe.repository.MyRoomRepository;
import com.ssafy.mademe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final MyRoomRepository myRoomRepository;
    private final EmotionCodeRepository emotionCodeRepository;
    private final UserRepository userRepository;

    public List<MonthDiaryResponse> getMonthDiaryStatus(User user, LocalDate date){
        Month now = LocalDateTime.now().getMonth();
        //System.out.println(now);
        List<Diary> monthInfo = diaryRepository.findMonthStatus(user, date);
        List<MonthDiaryResponse> result = new ArrayList<>();
        for(Diary d : monthInfo){
            LocalDate tmp = d.getCreateDate().toLocalDate();
            LocalDateTime start = tmp.atStartOfDay();
            LocalDateTime end = tmp.atTime(LocalTime.MAX);
            result.add(
                    MonthDiaryResponse.builder()
                    .diaryId(d.getId())
                    .date(d.getCreateDate().toLocalDate())
                    .emotion(d.getTotalEmotion().getId())
                    .doCommunity(myRoomRepository.existsById_UserAndInDateBetween(user, start, end))
                    .build()
            );
        }
        return result;
    }

    public SimpleDiaryResponse getSimpleDiaryInfo(Long id){
        Optional<Diary> diary = diaryRepository.findById(id);
        if(diary.isPresent()){
            return mapToSimpleDiary(diary.get());
        }
        throw new DiaryNotFoundException("해당 일기 정보가 없습니다.");
    }

    @Transactional
    public void addDiaryGPTLog(User user, DiaryGPTRequest request){
        Diary diary = new Diary();
        diary.setContent(request.getDailyData().getContent());
        diary.setDiaryScore(request.getDailyData().getDailyScore());
        diary.setKeyword(keywordListToString(request.getDailyData().getKeywords()));
        String emoName = request.getDailyData().getDailyEmotion();
        EmotionCode code = emotionCodeRepository.findFirstByEmotionName(emoName);
        diary.setTotalEmotion(code);
        List<EmotionLog> emoDatas = addEmotionLog(request);
        for(EmotionLog elog : emoDatas){
            diary.addEmotionLog(elog);
        }
        List<MBTILog> mbtiDatas = addMbtiLog(request);
        for(MBTILog mlog : mbtiDatas){
            diary.addMbtiLog(mlog);
            user.addMbtiLog(mlog);
        }
        user.addDiary(diary);
        diaryRepository.save(diary);
        userRepository.save(user);
    }

    public List<EmotionLog> addEmotionLog(DiaryGPTRequest diaryGPTRequest){
        List<EmotionLog> logs = new ArrayList<>();
        //EmotionLog emotionLog = new EmotionLog();
        List<DiaryGPTRequest.EmotionData> emotionDatas = diaryGPTRequest.getEmotionData();
        for(DiaryGPTRequest.EmotionData data : emotionDatas){
            EmotionCode code = emotionCodeRepository.findFirstByEmotionName(data.getEmotion());
            for(String sentence : data.getSentences()){
                EmotionLog emotionLog = new EmotionLog();
                emotionLog.setEmotionCode(code);
                emotionLog.setSentence(sentence);
                logs.add(emotionLog);
            }
        }
        return logs;
    }

    public List<MBTILog> addMbtiLog(DiaryGPTRequest diaryGPTRequest){
        List<MBTILog> logs = new ArrayList<>();
        List<DiaryGPTRequest.MBTIData> mbtiDatas = diaryGPTRequest.getMbtiData();
        for(DiaryGPTRequest.MBTIData data : mbtiDatas){
            for(String sentence : data.getSentences()){
                MBTILog mbtiLog = new MBTILog();
                mbtiLog.setMbtiCode(data.getMbti());
                mbtiLog.setSentence(sentence);
                logs.add(mbtiLog);
            }
        }
        return logs;
    }

    private SimpleDiaryResponse mapToSimpleDiary(Diary diary){
        return SimpleDiaryResponse.builder()
                .diaryId(diary.getId())
                .date(diary.getCreateDate().toLocalDate())
                .diaryScore(diary.getDiaryScore())
                .keyword(diary.getKeyword())
                .build();
    }

    private String keywordListToString(List<String> keywords){
        StringBuilder sb = new StringBuilder();
        for(String keyword : keywords){
            sb.append("#");
            sb.append(keyword);
        }
        return sb.toString();
    }
}
