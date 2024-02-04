package com.ssafy.mademe.controller;

import com.ssafy.mademe.dto.response.FollowingResponse;
import com.ssafy.mademe.dto.response.MyRoomInfoResponse;
import com.ssafy.mademe.dto.response.PagingInfoResponse;
import com.ssafy.mademe.dto.response.RoomDetailResponse;
import com.ssafy.mademe.entity.Room;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    //현재 내가 속해있는 커뮤니티 방 목록을 조회한다.(퇴장X)
    @GetMapping("/")
    public Map<String, Object> getMyKeepRoomList(@AuthenticationPrincipal User user, @PageableDefault(size = 3) Pageable pageable){
        Page<MyRoomInfoResponse> infos = roomService.getRoomInfos(user, null, false, pageable);
        for(MyRoomInfoResponse tmp : infos){
            tmp.setHashTags(roomService.getHashTagList(tmp.getRoomId()));
        }
        Map<String, Object> json = new HashMap<>();
        json.put("pagingInfo", PagingInfoResponse.builder()
                .totalPage(infos.getTotalPages()) //전체 페이지 수
                .pageNum(infos.getNumber()) //현재 페이지 번호
                .isFirst(infos.isFirst()) //이 페이지가 첫번째 페이지 인지
                .hasNext(infos.hasNext()) //다음 페이지가 존재하는지
                .build()
        );
        json.put("data", infos.get().collect(Collectors.toList()));
        return json;
    }

    //방의 세부정보를 조회한다
    @GetMapping("/detail/{roomId}")
    public RoomDetailResponse getRoomDetailInfo(@AuthenticationPrincipal User user, @PathVariable("roomId") Long roomId){
        Room roomInfo = roomService.getRoomDetailInfo(roomId);
        return mapToRoomDetail(roomInfo, roomService.getParticipants(user, roomId),
                roomService.getHashTagList(roomId));
    }

    private RoomDetailResponse mapToRoomDetail(Room room, List<FollowingResponse> participants,
                                               List<String> hashTags){
        return RoomDetailResponse.builder()
                .roomId(room.getId())
                .createDate(room.getCreateDate().toLocalDate())
                .type(room.getType())
                .ownerId(room.getOwnerId().getId())
                .title(room.getTitle())
                .intro(room.getIntro())
                .thumbnail(room.getThumbnail())
                .qId(room.getQuestion().getId())
                .participants(participants)
                .hashTags(hashTags)
                .build();
    }
}
