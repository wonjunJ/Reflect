package com.ssafy.mademe.service;

import com.ssafy.mademe.dto.UserMBTIMethod;
import com.ssafy.mademe.dto.response.*;
import com.ssafy.mademe.entity.*;
import com.ssafy.mademe.repository.InterestRepository;
import com.ssafy.mademe.repository.MBTILogRepository;
import com.ssafy.mademe.repository.OMBTICntRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterestService {
    private final InterestRepository interestRepository;
    private final MBTILogRepository mbtiLogRepository;
    private final OMBTICntRepository ombtiCntRepository;
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
        List<MBTICode> keys = new ArrayList<>(map.keySet());
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
        double percentNum = (map.get(key)/(double)(map.get(key) + map.get(oppositeKey))) * 100;
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
}
