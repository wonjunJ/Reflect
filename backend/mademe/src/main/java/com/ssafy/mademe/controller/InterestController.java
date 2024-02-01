package com.ssafy.mademe.controller;

import com.ssafy.mademe.dto.request.AddInterestRequest;
import com.ssafy.mademe.dto.response.InterestLsResponse;
import com.ssafy.mademe.entity.Interest;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.service.InterestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/")
    public InterestLsResponse getInterestDetailList(@AuthenticationPrincipal User user, @RequestParam("top") int top){
        return interestService.getDetailList(user, top);
    }

    @GetMapping("/mbti")
    public Map<String, Object> getMbtiDetail(@AuthenticationPrincipal User user){
        Map<String, Object> json = new HashMap<>();
        json.put("Mine", interestService.getMineMbtiDetail(user));
        json.put("Others", interestService.getOthersMbtiCnt(user));
        return json;
    }

    @PostMapping("/")
    public Map<String, String> addInterest(@AuthenticationPrincipal User user,
                                          @RequestBody AddInterestRequest addInterestRequest){
        interestService.addInterest(user, addInterestRequest);
        Map<String, String> json = new HashMap<>();
        json.put("state", "success");
        return json;
    }

    @DeleteMapping("/")
    public Map<String, String> deleteInterest(@RequestBody Interest itr){
        interestService.deleteInterest(itr); //Interest객체에 있는 id값을 받아온다.
        Map<String, String> json = new HashMap<>();
        json.put("state", "success");
        return json;
    }
}
