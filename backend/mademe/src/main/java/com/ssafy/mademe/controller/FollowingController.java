package com.ssafy.mademe.controller;

import com.ssafy.mademe.dto.response.FollowingResponse;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.service.FollowingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowingController {

    private final FollowingService followingService;

    @GetMapping("/{pgNum}")
    public List<FollowingResponse> getFollowingList(@PathVariable("pgNum") int pgNum, @AuthenticationPrincipal User user){
        return followingService.getFollowingList(user, pgNum);
    }

    @PostMapping("/")
    public String addFollowing(@AuthenticationPrincipal User user, @RequestBody User follow){
        followingService.addFollowing(user, follow);
        return "친구 추가 성공";
    }

    @DeleteMapping("/")
    public String delFollowing(@AuthenticationPrincipal User user, @RequestBody User follow){
        followingService.delFollowing(user, follow);
        return "친구 삭제 성공";
    }
}
