package com.ssafy.mademe.service;

import com.ssafy.mademe.dto.UserMBTIMethod;
import com.ssafy.mademe.dto.request.AddInterestRequest;
import com.ssafy.mademe.dto.response.*;
import com.ssafy.mademe.entity.*;
import com.ssafy.mademe.entity.embedded.EmdInterest;
import com.ssafy.mademe.exception.interest.ItrNotFoundException;
import com.ssafy.mademe.repository.InterestCodeRepository;
import com.ssafy.mademe.repository.InterestRepository;
import com.ssafy.mademe.repository.MBTILogRepository;
import com.ssafy.mademe.repository.OMBTICntRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterestService {
    private final InterestRepository interestRepository;
    private final MBTILogRepository mbtiLogRepository;
    private final OMBTICntRepository ombtiCntRepository;
    private final InterestCodeRepository interestCodeRepository;
    private final UserMBTIMethod userMBTIMethod = new UserMBTIMethod();

    public List<InterestResponse> getTop5Interest(User user){
        return interestRepository.findInterestList(user, 5, null);
    }

    public InterestLsResponse getDetailList(User user, int top){
        return new InterestLsResponse(
                interestRepository.findInterestList(user, top, Preference.LIKE),
                interestRepository.findInterestList(user, top, Preference.UNLIKE)
        );
    }

    public MyMBTIDetailResponse getMineMbtiDetail(User user){
        Map<MBTICode, Long> map = mbtiLogRepository.findMbtiByUser(user);
        List<MBTICode> keys = new ArrayList<>(map.keySet()); //존재하는 mbtiCode의 리스트
        List<MBTICntResponse> logRst = new ArrayList<>();
        for(MBTICode key : keys){
            logRst.add(
                    MBTICntResponse.builder()
                            .mbtiCode(key)
                            .cnt(map.get(key))
                            .percent(calPercent(map, key))
                            .build()
            );
        }
        return MyMBTIDetailResponse.builder()
                .mbti(userMBTIMethod.getMBTIs(map))
                .mbtiInfo(logRst)
                .build();
    }

    public List<OMBTICntResponse> getOthersMbtiCnt(User user){
        //stream을 활용해서 List<OMBTICnt>를 List<OMBTICntResponse>로 매핑한다.
        return ombtiCntRepository.findByCompositeKey_User(user)
                .stream()
                .map(this::mapToOMBTICntResponse)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getMbtiLogInfos(User user, MBTICode code, Pageable pageable){
        Page<MBTILog> mbtiLogs = mbtiLogRepository.findByUserAndMbtiCodeOrderByDiaryCreateDateDesc(user, code, pageable);
        List<DiaryLogResponse> logdata = new ArrayList<>();
        for(MBTILog logs : mbtiLogs){
            logdata.add(mapToDiaryLog(logs));
        }
        Map<String, Object> json = new HashMap<>();
        //페이징 처리를 위한 Page객체로 변환
        Page<DiaryLogResponse> result = new PageImpl<>(logdata, pageable, mbtiLogs.getTotalElements());
        json.put("pagingInfo", PagingInfoResponse.builder()
                        .totalPage(result.getTotalPages())
                        .pageNum(result.getNumber())
                        .isFirst(result.isFirst())
                        .hasNext(result.hasNext())
                        .build()
        );
        json.put("data", logdata);
        return json;
    }

    public InterestResponse addInterestByUser(User user, AddInterestRequest addInterestRequest){
        Interest itr = interestRepository.findFirstByUserAndCompositeKey_PreferenceAndCompositeKey_InterestCode_HobbyName(user,
                addInterestRequest.getPreference(), addInterestRequest.getName());
        Integer fourth = interestRepository.findFourthCount(user, addInterestRequest.getPreference());
        if(itr != null){ //추가하려는 관심사가 이미 나한테 있으므로 그냥 값을 수정
            return addFourthLocation(itr, fourth);
        }
        //새로운 관심사를 추가
        itr = new Interest();
        itr.setUser(user);
        InterestCode codeId = checkInterestCode(addInterestRequest.getName());
        EmdInterest emd = new EmdInterest(codeId, addInterestRequest.getPreference());
        itr.setCompositeKey(emd);
        return addFourthLocation(itr, fourth);
    }

    public void deleteInterest(Interest itr){
        Optional<Interest> interest = interestRepository.findById(itr.getId());
        if(interest.isPresent()){
            interest.get().setCnt(0);
            interestRepository.save(interest.get());
            return;
        }
        throw new ItrNotFoundException("삭제할 관심사가 존재하지 않습니다.");
    }

    public int calPercent(Map<MBTICode, Long> map, MBTICode key){
        MBTICode oppositeKey;
        if(key.equals(MBTICode.I) || key.equals(MBTICode.E)){
            oppositeKey = key.equals(MBTICode.I) ? MBTICode.E : MBTICode.I;
        } else if (key.equals(MBTICode.N) || key.equals(MBTICode.S)) {
            oppositeKey = key.equals(MBTICode.N) ? MBTICode.S : MBTICode.N;
        } else if (key.equals(MBTICode.F) || key.equals(MBTICode.T)) {
            oppositeKey = key.equals(MBTICode.F) ? MBTICode.T : MBTICode.F;
        } else {
            oppositeKey = key.equals(MBTICode.J) ? MBTICode.P : MBTICode.J;
        }
        Long oppoVal = map.containsKey(oppositeKey) ? map.get(oppositeKey) : 0;
        double percentNum = (map.get(key)/(double)(map.get(key) + oppoVal)) * 100;
        System.out.println(percentNum);
        return (int) Math.round(percentNum);
    }

    //OMBTICnt를 OMBTICntResponse 타입 객체로 매핑해주는 함수
    private OMBTICntResponse mapToOMBTICntResponse(OMBTICnt input) {
        OMBTICntResponse response = new OMBTICntResponse();
        response.setMbti(input.getCompositeKey().getMbti());
        response.setCnt(input.getCnt());
        return response;
    }

    //해당하는 관심사명에 대해서 InterestCode 테이블에 값이 존재하는지를 체크(없으면 값을 새로 저장하고 리턴)
    public InterestCode checkInterestCode(String name){
        InterestCode codeId = interestCodeRepository.findFirstByHobbyName(name);
        if(codeId == null){
            codeId = new InterestCode();
            codeId.setHobbyName(name);
            return interestCodeRepository.save(codeId);
        }
        return codeId;
    }

    private InterestResponse addFourthLocation(Interest itr, Integer fourth){ //관심사를 4번째자리 다음으로 추가하는 로직
        if(fourth == null || fourth == 0 || fourth == 1){
            itr.setCnt(1);
        }else{
            itr.setCnt(fourth-1);
        }
        return mapToInterestResponse(interestRepository.save(itr));
    }

    private DiaryLogResponse mapToDiaryLog(MBTILog mbtiLog){
        return DiaryLogResponse.builder()
                .id(mbtiLog.getId())
                .diaryId(mbtiLog.getDiary().getId())
                .createDate(mbtiLog.getDiary().getCreateDate())
                .sentence(mbtiLog.getSentence())
                .build();
    }

    private InterestResponse mapToInterestResponse(Interest interest) {
        return InterestResponse.builder()
                .interestId(interest.getId()) //관심사 코드의 아이디가 아니라 내 관심사에 대한 아이디값
                .interestName(interest.getCompositeKey().getInterestCode().getHobbyName())
                .preference(interest.getCompositeKey().getPreference())
                .cnt(interest.getCnt())
                .build();
    }
}
