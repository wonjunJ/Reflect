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
        //List<Diary> monthInfo = diaryRepository.findMonthStatus(user, date);
        List<MonthDiaryResponse> result = diaryRepository.findMonthStatus(user, date);
        for(MonthDiaryResponse d : result){ //각 일자에 대한 커뮤니티 상태를 조회한다.
            LocalDate tmp = d.getDate();
            LocalDateTime start = tmp.atStartOfDay();
            LocalDateTime end = tmp.atTime(LocalTime.MAX);
            d.setDoCommunity(myRoomRepository.existsById_UserAndInDateBetween(user, start, end));
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
    public User addDiaryGPTLog(User user, DiaryGPTRequest request){
        Diary diary = new Diary();
        diary.setContent(request.getDailyData().getContent());
        diary.setDiaryScore(request.getDailyData().getDaily_score());
        diary.setKeyword(keywordListToString(request.getDailyData().getKeywords()));
        String emoName = request.getDailyData().getDaily_emotion(); //그 날의 총괄 감정
        System.out.println("이게???" + emoName);
        EmotionCode code = emotionCodeRepository.findFirstByEmotionName(emoName);
        System.out.println(code.getId());
        diary.setTotalEmotion(code);
        List<EmotionLog> emoDatas = addEmotionLog(request);
        for(EmotionLog elog : emoDatas){
            diary.addEmotionLog(elog);
        }
        List<MBTILog> mbtiDatas = addMbtiLog(request);
        for(MBTILog mlog : mbtiDatas){
            diary.addMbtiLog(mlog);
            mlog.setUser(user);
        }
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDateTime start = yesterday.atStartOfDay();
        LocalDateTime end = yesterday.atTime(LocalTime.MAX);
        if(diaryRepository.existsByUserAndCreateDateBetween(user, start, end)){ //어제 날짜로 쓴 일기가 있다.
            userRepository.increaseCntById(user.getId()); //일기 연속 작성 일자에 +1
        }else {
            user.setRecordTime(0);
        }
        diary.setUser(user);
        diaryRepository.save(diary);
        return userRepository.save(user);
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
                System.out.println(mbtiLog.getMbtiCode());
                logs.add(mbtiLog);
            }
        }
        System.out.println(logs.size());
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
