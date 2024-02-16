package com.ssafy.mademe.controller;

import com.ssafy.mademe.dto.response.DiaryLogResponse;
import com.ssafy.mademe.dto.response.FollowingResponse;
import com.ssafy.mademe.dto.response.PagingInfoResponse;
import com.ssafy.mademe.dto.response.UserInfoResponse;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.service.FollowingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowingController {

    private final FollowingService followingService;

    //내가 팔로우하고 있는 회원의 리스트 정보 얻기
    @GetMapping("")
    public Map<String, Object> getFollowingList(@PageableDefault(size = 3) Pageable pageable, @AuthenticationPrincipal User user){
        Page<FollowingResponse> pgRst = followingService.getFollowingList(user, pageable);
        Map<String, Object> json = new HashMap<>();
        json.put("pagingInfo", PagingInfoResponse.builder()
                .totalPage(pgRst.getTotalPages()) //전체 페이지 수
                .pageNum(pgRst.getNumber()) //현재 페이지 번호
                .isFirst(pgRst.isFirst()) //이 페이지가 첫번째 페이지 인지
                .hasNext(pgRst.hasNext()) //다음 페이지가 존재하는지
                .build()
        );
        json.put("data", pgRst.get().collect(Collectors.toList()));
        return json;
    }

    //다른 회원의 세부 정보 얻기(팔로우 하고 있는 사람이 아닌사람도 얻을 수 있다.)
    @GetMapping("/detail")
    public FollowingResponse getFollowingInfo(@AuthenticationPrincipal User user, @RequestParam("id") Long id){
        return followingService.getFollowingInfo(user, id);
    }

    @PostMapping("")
    public String addFollowing(@AuthenticationPrincipal User user, @RequestBody User follow){
        followingService.addFollowing(user, follow);
        return "친구 추가 성공";
    }

    @DeleteMapping("")
    public String delFollowing(@AuthenticationPrincipal User user, @RequestBody User follow){
        followingService.delFollowing(user, follow);
        return "친구 삭제 성공";
    }
}
