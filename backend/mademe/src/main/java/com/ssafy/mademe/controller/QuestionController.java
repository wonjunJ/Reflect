package com.ssafy.mademe.controller;

import com.ssafy.mademe.entity.QuestionRecord;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("/")
    public List<QuestionRecord> getMyQRecord(@AuthenticationPrincipal User user){
        return questionService.getMyQRecord(user);
    }
}
