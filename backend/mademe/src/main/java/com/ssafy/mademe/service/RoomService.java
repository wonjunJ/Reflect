package com.ssafy.mademe.service;

import com.ssafy.mademe.dto.response.FollowingResponse;
import com.ssafy.mademe.dto.response.MyRoomInfoResponse;
import com.ssafy.mademe.dto.response.RoomDetailResponse;
import com.ssafy.mademe.entity.HashTag;
import com.ssafy.mademe.entity.Room;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.exception.room.RoomNotFoundException;
import com.ssafy.mademe.repository.FollowingRepository;
import com.ssafy.mademe.repository.HashTagRepository;
import com.ssafy.mademe.repository.MyRoomRepository;
import com.ssafy.mademe.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final MyRoomRepository myRoomRepository;
    private final HashTagRepository hashTagRepository;
    private final FollowingRepository followingRepository;
    private final RoomRepository roomRepository;

    public Page<MyRoomInfoResponse> getRoomInfos(User user, LocalDate indate, Boolean isExit, Pageable pageable){
        return myRoomRepository.findRoomInfoListByUser(user, indate, isExit, pageable);
    }

    public Room getRoomDetailInfo(Long roomId){
        Optional<Room> roomInfo = roomRepository.findById(roomId);
        if(roomInfo.isPresent()){
            return roomInfo.get();
        }
        throw new RoomNotFoundException("해당하는 방 정보를 찾을 수 없습니다.");
    }

    public List<String> getHashTagList(Long roomId){
        List<HashTag> list = hashTagRepository.findById_Room_Id(roomId);
        List<String> rst = new ArrayList<>();
        for(HashTag tag : list){
            rst.add(tag.getId().getHashCode().getHashName());
        }
        return rst;
    }

    public List<FollowingResponse> getParticipants(User user, Long roomId){
        Room rid = new Room();
        rid.setId(roomId);
        List<FollowingResponse> list = myRoomRepository.findParticipantsByRoom(rid);
        //두번 반복문을 도는게 별로긴 한데, queryDsl단에서 팔로우하는 사용자인지 확인하는 방법이 없는듯
        for(FollowingResponse part : list){
            User follow = new User();
            follow.setId(part.getUserId());
            part.set_follow(followingRepository.existsById_UserIdAndId_FollowId(user, follow));
        }
        return list;
    }
}
