package com.ssafy.mademe.controller;

import com.ssafy.mademe.dto.response.RoomInfoResponse;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.repository.HashTagRepository;
import com.ssafy.mademe.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    //현재 내가 속해있는 커뮤니티 방 목록을 조회한다.(퇴장X)
    @GetMapping("/")
    public List<RoomInfoResponse> getMyKeepRoomList(@AuthenticationPrincipal User user, Pageable pageable){
        List<RoomInfoResponse> infos = roomService.getRoomInfos(user, null, false, pageable);
        for(RoomInfoResponse tmp : infos){
            tmp.setHashTags(roomService.getHashTagList(tmp.getRoomId()));
        }
        return infos;
    }
}
