package com.ssafy.mademe.controller;

import com.ssafy.mademe.dto.request.ChangeOwnerRequest;
import com.ssafy.mademe.dto.request.RoomRequest;
import com.ssafy.mademe.dto.response.*;
import com.ssafy.mademe.entity.Room;
import com.ssafy.mademe.entity.RoomMode;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.exception.room.NotAuthorizedException;
import com.ssafy.mademe.repository.RoomRepository;
import com.ssafy.mademe.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;
    private final RoomRepository roomRepository;

    //현재 내가 속해있는 커뮤니티 방 목록을 조회한다.(퇴장X)
    @GetMapping("/mine")
    public Map<String, Object> getMyKeepRoomList(@AuthenticationPrincipal User user, @PageableDefault(size = 3) Pageable pageable){
        Page<MyRoomInfoResponse> infos = roomService.getRoomInfos(user, null, false, pageable);
        for(MyRoomInfoResponse tmp : infos){
            tmp.setMode(roomService.getRoomModes(tmp.getRoomId()));
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
                roomService.getHashTagList(roomId), roomService.getRoomModes(roomId));
    }

    //방 전체 목록 조회(검색까지)
    @GetMapping("")  //@RequestParam에 defaultValue = ""는 filter가 넘어오지 않았을시 null이 아니라 빈 리스트로 처리하기 위해
    public Map<String, Object> getTotalRoomList(@RequestParam(value = "filter", required = false, defaultValue = "") List<String> filter,
                                                @RequestParam(value = "search", required = false) String searchWord,
                                                @RequestParam("from") int from, @RequestParam("to") int to,
                                                @PageableDefault(size = 10) Pageable pageable){
        Page<RoomListResponse> roomInfos = roomService.getTotalRoomList(filter, searchWord, from, to, pageable);
//        List<Room> tmp2 = roomRepository.findAll();
//        for(Room rm : tmp2){
//            System.out.println("방 타입은 " + rm.getType());
//        }
        for(RoomListResponse tmp : roomInfos){
            System.out.println("여기는 없다ㅠㅠ");
            tmp.setMode(roomService.getRoomModes(tmp.getRoomId()));
            tmp.setHashTags(roomService.getHashTagList(tmp.getRoomId()));
        }
        Map<String, Object> json = new HashMap<>();
        json.put("pagingInfo", PagingInfoResponse.builder()
                .totalPage(roomInfos.getTotalPages()) //전체 페이지 수
                .pageNum(roomInfos.getNumber()) //현재 페이지 번호
                .isFirst(roomInfos.isFirst()) //이 페이지가 첫번째 페이지 인지
                .hasNext(roomInfos.hasNext()) //다음 페이지가 존재하는지
                .build()
        );
        json.put("data", roomInfos.get().collect(Collectors.toList()));
        return json;
    }

    //커뮤니티 방에 참가를 한다.
    @PostMapping("/attend")
    public Map<String, String> joinRoom(@AuthenticationPrincipal User user, @RequestBody Room room){ //방 아이디만 받는다.
        roomService.joinRoom(user, room);
        roomService.increaseNumById(room.getId());
        Map<String, String> json = new HashMap<>();
        json.put("state", "success");
        return json;
    }

    //방장을 위임한다.
    @PutMapping("/empower")
    public Map<String, String> changeOwner(@AuthenticationPrincipal User origin, @RequestBody ChangeOwnerRequest request){
        try {
            roomService.changeOwner(origin, request.getRoomId(), request.getOwnerId());
            Map<String, String> json = new HashMap<>();
            json.put("state", "success");
            return json;
        }catch (Exception e){
            throw new NotAuthorizedException("필요한 정보가 없습니다.");
        }
    }

    //방 생성하기
    @PostMapping("/create")
    public RoomDetailResponse createRoom(@AuthenticationPrincipal User user, @RequestBody RoomRequest request){
        Room room = roomService.createRoom(user, request);
        roomService.storeHashTags(room, request.getHashTags());
        return mapToRoomDetail(room, null, request.getHashTags(), request.getMode());
    }

    //방 추천하기
    @GetMapping("/recommend")
    public List<RoomDetailResponse> recommendRoom(@AuthenticationPrincipal User user){
        return roomService.recommendRoom(user);
    }

    //방 탈퇴하기(방 참가 상태(채팅 화면)에서 탈퇴하기를 누를때)
    @DeleteMapping("/")
    public Map<String, String> quitRoom(@AuthenticationPrincipal User user, @RequestBody Room roomId){ //방 아이디 값만 받는다.
        roomService.quitRoom(user, roomId);
        //roomService.decreaseNumById(roomId.getId());
        Map<String, String> json = new HashMap<>();
        json.put("state", "success");
        return json;
    }

    //채팅만 종료(방에 참여는 유지)
    @DeleteMapping("/finish")
    public Map<String, String> finishChatting(@AuthenticationPrincipal User user, @RequestBody Room roomId){
        roomService.decreaseNumById(roomId.getId());
        Map<String, String> json = new HashMap<>();
        json.put("state", "success");
        return json;
    }

    private RoomDetailResponse mapToRoomDetail(Room room, List<FollowingResponse> participants,
                                               List<String> hashTags, List<RoomMode> modes){
        return RoomDetailResponse.builder()
                .roomId(room.getId())
                .createDate(room.getCreateDate().toLocalDate())
                .type(room.getType())
                .mode(modes)
                .ownerId(room.getOwnerId().getId())
                .title(room.getTitle())
                .intro(room.getIntro())
                .thumbnail(room.getThumbnail())
                .qId(room.getQuestion() == null ? null : room.getQuestion().getId())
                .participants(participants)
                .hashTags(hashTags)
                .build();
    }
}
