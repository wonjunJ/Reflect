package com.ssafy.mademe.controller;

import com.ssafy.mademe.dto.request.QuestionRequest;
import com.ssafy.mademe.dto.response.QuestionResponse;
import com.ssafy.mademe.entity.QuestionCode;
import com.ssafy.mademe.entity.QuestionRecord;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    //내가 답변한 백문백답 전체 리스트
    @GetMapping("/")
    public List<QuestionResponse> getMyQRecords(@AuthenticationPrincipal User user){
        return questionService.getMyQRecords(user);
    }

    //나의 백문백답 질문 답변 세부정보
    @GetMapping("/detail/{answerId}")
    public QuestionResponse getMyQAnswer(@PathVariable("answerId") Long answerId){
        return questionService.getMyQAnswer(answerId);
    }

    //백문백답 질문 답변 추가
    @PostMapping("/")
    public QuestionResponse addMyQAnswer(@AuthenticationPrincipal User user, @RequestBody QuestionRequest request){
        return questionService.addMyQAnswer(user, request);
    }

    //질문에 대한 나의 답변을 수정
    @PutMapping("/")
    public QuestionResponse modifyMyAnswer(@AuthenticationPrincipal User user, @RequestBody QuestionRequest request){
        return questionService.modifyMyAnswer(user, request);
    }

    //질문에 대한 나의 답변을 삭제
    @DeleteMapping("/")
    public Map<String, String> deleteMyAnswer(@AuthenticationPrincipal User user, @RequestBody QuestionRequest request){
        questionService.deleteMyAnswer(user, request);
        Map<String, String> json = new HashMap<>();
        json.put("state", "success");
        return json;
    }
}
