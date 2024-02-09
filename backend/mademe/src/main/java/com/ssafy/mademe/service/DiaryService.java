package com.ssafy.mademe.service;

import com.ssafy.mademe.dto.gpt.request.DiaryGPTRequest;
import com.ssafy.mademe.dto.response.MonthDiaryResponse;
import com.ssafy.mademe.dto.response.SimpleDiaryResponse;
import com.ssafy.mademe.entity.*;
import com.ssafy.mademe.entity.embedded.EmdInterest;
import com.ssafy.mademe.exception.diary.DiaryNotFoundException;
import com.ssafy.mademe.repository.*;
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
    private final InterestRepository interestRepository;
    private final InterestService interestService;

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
        addInterestByGPT(user, request.getDailyData().getInterestData());
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDateTime start = yesterday.atStartOfDay();
        LocalDateTime end = yesterday.atTime(LocalTime.MAX);
        if(diaryRepository.existsByUserAndCreateDateBetween(user, start, end)){ //어제 날짜로 쓴 일기가 있다.
            userRepository.increaseCntById(user.getId()); //일기 연속 작성 일자에 +1
        }else {
            user.setRecordTime(1);
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
                String code = data.getMbti().equals("INtuition") ?
                        Character.toString(data.getMbti().charAt(1)) :
                        Character.toString(data.getMbti().charAt(0));
                mbtiLog.setMbtiCode(MBTICode.valueOf(code));
                mbtiLog.setSentence(sentence);
                System.out.println(mbtiLog.getMbtiCode());
                logs.add(mbtiLog);
            }
        }
        System.out.println(logs.size());
        return logs;
    }

    public void addInterestByGPT(User user, List<DiaryGPTRequest.DailyData.InterestData> interestData){
        for(DiaryGPTRequest.DailyData.InterestData data : interestData){
            Interest itr = interestRepository.findFirstByUserAndCompositeKey_PreferenceAndCompositeKey_InterestCode_HobbyName(user,
                    data.getPreference(), data.getInterest());
            if(itr != null){ //추가하려는 관심사가 이미 나한테 있으므로 그냥 값을 수정
                interestRepository.increaseCntById(itr.getId());
            }else {
                //새로운 관심사를 추가
                itr = new Interest();
                itr.setUser(user);
                itr.setCnt(1);
                InterestCode codeId = interestService.checkInterestCode(data.getInterest());
                EmdInterest emd = new EmdInterest(codeId, data.getPreference());
                itr.setCompositeKey(emd);
            }
            interestRepository.save(itr);
        }
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
