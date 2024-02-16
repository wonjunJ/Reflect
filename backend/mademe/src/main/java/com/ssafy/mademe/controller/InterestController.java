package com.ssafy.mademe.controller;

import com.ssafy.mademe.dto.request.AddInterestRequest;
import com.ssafy.mademe.dto.response.DiaryLogResponse;
import com.ssafy.mademe.dto.response.InterestLsResponse;
import com.ssafy.mademe.dto.response.InterestResponse;
import com.ssafy.mademe.entity.Interest;
import com.ssafy.mademe.entity.MBTICode;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.service.InterestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/interest")
@RequiredArgsConstructor
public class InterestController {
    private final InterestService interestService;

    //관심사 세부 정보
    @GetMapping("")
    public InterestLsResponse getInterestDetailList(@AuthenticationPrincipal User user, @RequestParam("top") int top){
        //int top = Integer.parseInt(tmp);
        return interestService.getDetailList(user, top);
    }

    //MBTI 분석 그래프 정보
    @GetMapping("/mbti")
    public Map<String, Object> getMbtiDetail(@AuthenticationPrincipal User user){
        Map<String, Object> json = new HashMap<>();
        json.put("Mine", interestService.getMineMbtiDetail(user));
        json.put("Others", interestService.getOthersMbtiCnt(user));
        return json;
    }

    //MBTI 일기 분석 정보
    @GetMapping("/mbti/{code}")
    public Map<String, Object> getMbtiLogInfo(@AuthenticationPrincipal User user, @PathVariable("code") MBTICode code,
                                           @PageableDefault(size = 5) Pageable pageable){
        return interestService.getMbtiLogInfos(user, code, pageable);
    }

    //관심사 추가
    @PostMapping("/")
    public InterestResponse addInterest(@AuthenticationPrincipal User user,
                                        @RequestBody AddInterestRequest addInterestRequest){
        return interestService.addInterestByUser(user, addInterestRequest);
    }

    //관심사 삭제
    @DeleteMapping("/")
    public Map<String, String> deleteInterest(@RequestBody Interest itr){
        interestService.deleteInterest(itr); //Interest객체에 있는 id값을 받아온다.
        Map<String, String> json = new HashMap<>();
        json.put("state", "success");
        return json;
    }
}
