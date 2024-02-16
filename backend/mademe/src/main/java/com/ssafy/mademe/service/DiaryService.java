package com.ssafy.mademe.service;

import com.ssafy.mademe.dto.gpt.request.DiaryGPTRequest;
import com.ssafy.mademe.dto.request.Message;
import com.ssafy.mademe.dto.response.MonthDiaryResponse;
import com.ssafy.mademe.dto.response.SimpleDiaryResponse;
import com.ssafy.mademe.entity.*;
import com.ssafy.mademe.entity.embedded.EmdInterest;
import com.ssafy.mademe.entity.embedded.EmdTitle;
import com.ssafy.mademe.exception.diary.DiaryNotFoundException;
import com.ssafy.mademe.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private final TitleCodeRepository titleCodeRepository;
    private final UserTitleRepository userTitleRepository;
    private final MBTILogRepository mbtiLogRepository;
    private final SimpMessageSendingOperations messageTemplate;

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

    public void addDiaryImg(Diary diary){
        Optional<Diary> diaryInfo = diaryRepository.findById(diary.getId());
        if(diaryInfo.isPresent()){
            diaryInfo.get().setDiaryImg(diary.getDiaryImg());
            diaryRepository.save(diaryInfo.get());
            return;
        }
        throw new DiaryNotFoundException("해당 일기 정보가 없습니다.");
    }

    @Transactional
    public Diary addDiaryGPTLog(User user, DiaryGPTRequest request){
        Diary diary = new Diary();
        diary.setContent(request.getDailyData().getContent());
        diary.setDiaryScore(request.getDailyData().getDaily_score());
        diary.setKeyword(keywordListToString(request.getDailyData().getKeywords()));
        String emoName = request.getDailyData().getDaily_emotion(); //그 날의 총괄 감정
        System.out.println("이게???" + emoName);
        if("Normal".equals(emoName) || "Neutral".equals(emoName)){
            emoName = "None";
        }
        EmotionCode code = emotionCodeRepository.findFirstByEmotionName(emoName);
//        System.out.println(code.getId());
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
        Diary resultId = diaryRepository.save(diary);

        //포인트 획득
        int curPoint = user.getPoint();
        user.setPoint(curPoint + 10);
        getDiaryTitle(user);
        getMbtiTitle(user);
        userRepository.save(user);
        return resultId;
    }

    //일기 관련 칭호 획득
    public void getDiaryTitle(User user){
        Optional<TitleCode> tcode;
        if(user.getRecordTime() == 10){
            tcode = titleCodeRepository.findById(1L);
        } else if (user.getRecordTime() == 30) {
            tcode = titleCodeRepository.findById(2L);
        } else if (user.getRecordTime() == 100) {
            tcode = titleCodeRepository.findById(3L);
        } else { //칭호 획득 실패
            return;
        }
        EmdTitle emd = new EmdTitle(user, tcode.get());
        UserTitle ut = new UserTitle();
        ut.setId(emd);
        userTitleRepository.save(ut);

        //칭호 획득시에는 Alram 테이블에 저장할 필요X(칭호를 한번 획득한 이후에는 알람을 보낼지 말지 정해야 할듯)
        messageTemplate.convertAndSend("/alram/msg-to/" + user.getId(),
                Message.builder()
                        .type(1)
                        .senderId(user.getId())
                        .senderNick("관리자")
                        .toId(user.getId())
                        .msg(tcode.get().getTitleName() + " 칭호를 획득하였습니다.")
                        .build()
        );
    }

    public List<EmotionLog> addEmotionLog(DiaryGPTRequest diaryGPTRequest){
        List<EmotionLog> logs = new ArrayList<>();
        //EmotionLog emotionLog = new EmotionLog();
        List<DiaryGPTRequest.EmotionData> emotionDatas = diaryGPTRequest.getEmotionData();
        for(DiaryGPTRequest.EmotionData data : emotionDatas){
            String emoName = data.getEmotion();
            if("Normal".equals(emoName) || "Neutral".equals(emoName)){
                emoName = "None";
            }
            EmotionCode code = emotionCodeRepository.findFirstByEmotionName(emoName);
            if(code == null) continue;
            for(String sentence : data.getSentences()){
                if ("None".equals(sentence)) {continue;}
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
                if ("None".equals(sentence)) {continue;}
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

    //MBTI 관련 칭호 획득
    public void getMbtiTitle(User user){
        Map<MBTICode, Long> map = mbtiLogRepository.findMbtiByUser(user);
        List<MBTICode> keys = new ArrayList<>(map.keySet()); //존재하는 mbtiCode의 리스트
        boolean flag = false;
        for(MBTICode key : keys){
            if(key == MBTICode.E){
                flag = checkMbtiTitle(user, map.get(key), 5L);
            } else if (key == MBTICode.I) {
                flag = checkMbtiTitle(user, map.get(key), 6L);
            } else if (key == MBTICode.N) {
                flag = checkMbtiTitle(user, map.get(key), 7L);
            } else if (key == MBTICode.S) {
                flag = checkMbtiTitle(user, map.get(key), 8L);
            } else if (key == MBTICode.T) {
                flag = checkMbtiTitle(user, map.get(key), 9L);
            } else if (key == MBTICode.F) {
                flag = checkMbtiTitle(user, map.get(key), 10L);
            } else if (key == MBTICode.P) {
                flag = checkMbtiTitle(user, map.get(key), 11L);
            } else if (key == MBTICode.J) {
                flag = checkMbtiTitle(user, map.get(key), 12L);
            }
        }
        if(flag){ //MBTI관련 칭호를 하나라도 획득을 했다.
            messageTemplate.convertAndSend("/alram/msg-to/" + user.getId(),
                    Message.builder()
                            .type(1)
                            .senderId(user.getId())
                            .senderNick("관리자")
                            .toId(user.getId())
                            .msg("MBTI관련 칭호를 획득하였습니다.")
                            .build()
            );
        }
    }

    public boolean checkMbtiTitle(User user, Long cnt, Long id){
        if(cnt >= 10){
            Optional<TitleCode> tcode = titleCodeRepository.findById(id);
            EmdTitle emd = new EmdTitle(user, tcode.get());
            if(userTitleRepository.existsById(emd)){ //이미 이 칭호를 획득한 상태
                return false;
            }
            UserTitle ut = new UserTitle();
            ut.setId(emd);
            userTitleRepository.save(ut);
            return true; //칭호를 새로 획득했을 경우에만 true
        }
        return false;
    }

    public void addInterestByGPT(User user, List<DiaryGPTRequest.DailyData.InterestData> interestData){
        for(DiaryGPTRequest.DailyData.InterestData data : interestData){
            if("None".equals(data.getInterest())){
                continue;
            }
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
        getInterestTitle(user);
    }

    //관심사 관련 칭호 획득
    public void getInterestTitle(User user){
        int cnt = interestRepository.countByUserAndCompositeKey_Preference(user, Preference.LIKE);
        if(cnt >= 5){ //취미 부자 칭호를 획득하는 조건
            Optional<TitleCode> tcode = titleCodeRepository.findById(4L);
            EmdTitle emd = new EmdTitle(user, tcode.get());
            if(userTitleRepository.existsById(emd)){ //이미 이 칭호를 획득한 상태
                return;
            }
            UserTitle ut = new UserTitle();
            ut.setId(emd);
            userTitleRepository.save(ut);

            //칭호 획득시에는 Alram 테이블에 저장할 필요X
            messageTemplate.convertAndSend("/alram/msg-to/" + user.getId(),
                    Message.builder()
                            .type(1)
                            .senderId(user.getId())
                            .senderNick("관리자")
                            .toId(user.getId())
                            .msg(tcode.get().getTitleName() + " 칭호를 획득하였습니다.")
                            .build()
            );
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
