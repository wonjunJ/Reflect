package com.ssafy.mademe.controller;

import com.ssafy.mademe.dto.response.FollowingResponse;
import com.ssafy.mademe.dto.response.UserInfoResponse;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.service.FollowingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowingController {

    private final FollowingService followingService;

    //내가 팔로우하고 있는 회원의 리스트 정보 얻기
    @GetMapping("/")
    public List<FollowingResponse> getFollowingList(@PageableDefault(size = 3) Pageable pageable, @AuthenticationPrincipal User user){
        return followingService.getFollowingList(user, pageable);
    }

    //다른 회원의 세부 정보 얻기(팔로우 하고 있는 사람이 아닌사람도 얻을 수 있다.)
    @GetMapping("/detail")
    public FollowingResponse getFollowingInfo(@AuthenticationPrincipal User user, @RequestParam("id") Long id){
        return followingService.getFollowingInfo(user, id);
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
