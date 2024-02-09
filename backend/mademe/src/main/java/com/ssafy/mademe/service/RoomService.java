package com.ssafy.mademe.service;

import com.ssafy.mademe.dto.request.RoomRequest;
import com.ssafy.mademe.dto.response.FollowingResponse;
import com.ssafy.mademe.dto.response.MyRoomInfoResponse;
import com.ssafy.mademe.dto.response.RoomDetailResponse;
import com.ssafy.mademe.dto.response.RoomListResponse;
import com.ssafy.mademe.entity.*;
import com.ssafy.mademe.entity.embedded.EmdHash;
import com.ssafy.mademe.entity.embedded.EmdRoom;
import com.ssafy.mademe.exception.room.NotAuthorizedException;
import com.ssafy.mademe.exception.room.RoomNotFoundException;
import com.ssafy.mademe.exception.user.InvalidCodeException;
import com.ssafy.mademe.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final QuestionCodeRepository questionCodeRepository;
    private final HashCodeRepository hashCodeRepository;

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

    public Page<RoomListResponse> getTotalRoomList(List<String> filter, String searchWord, int from, int to, Pageable pageable){
        return roomRepository.findTotalRoomList(filter, searchWord, from, to, pageable);
    }

    public void joinRoom(User user, Room room){
        MyRoom myRoom = myRoomRepository.findFirstById_UserAndId_Room(user, room);
        if(myRoom != null){
            if(myRoom.isExit()){
                myRoom.setExit(false);
            }else { //이미 이 방에 참가를 한 상태이다.(가입되어 있는 상태)
                return;
            }
        } else { //이 방에 처음 참가를 한다.(과거에 탈퇴를 한 이력도 없다.)
            myRoom = new MyRoom();
            myRoom.setId(new EmdRoom(user, room));
        }
        myRoomRepository.save(myRoom);
    }

    public void changeOwner(User origin, Long roomId, Long owner){
        Room room = roomRepository.findFirstByIdAndOwnerId(roomId, origin);
        if(room != null){
            User newOwner = new User();
            newOwner.setId(owner);
            room.setOwnerId(newOwner);
            roomRepository.save(room);
        }
        throw new NotAuthorizedException("방장을 이임할 권한이 없습니다.");
    }

    public Room createRoom(User user, RoomRequest request){
        Room room = new Room();
        room.setTotalNum(request.getTotalNum());
        room.setType(request.getType());
        room.setMode(request.getMode());
        room.setTitle(request.getTitle());
        room.setIntro(request.getIntro());
        room.setOwnerId(user);
        room.setThumbnail(request.getThumbnail());
        Optional<QuestionCode> qid = questionCodeRepository.findById(request.getQuestionId());
        qid.ifPresent(room::setQuestion);
        return roomRepository.save(room);
    }

    public void storeHashTags(Room room, List<String> hashtags){
        for(String tag : hashtags){
            HashCode code = hashCodeRepository.findFirstByHashName(tag);
            if(code == null){
                code = new HashCode();
                code.setHashName(tag);
                code = hashCodeRepository.save(code);
            }
            HashTag hashTag = new HashTag();
            hashTag.setId(new EmdHash(room, code));
            hashTagRepository.save(hashTag);
        }
    }

    @Transactional
    public void quitRoom(User user, Room room){
        MyRoom myRoom = myRoomRepository.findFirstById_UserAndId_Room(user, room);
        if(myRoom == null){
            throw new InvalidCodeException("잘못된 요청입니다.");
        }
        myRoom.setExit(true);
        if(myRoomRepository.countById_RoomAndIsExitIsFalse(room) == 1){ //현재 방에 가입되어 있는 사람의 수
            Optional<Room> tmp = roomRepository.findById(room.getId());
            if(tmp.isEmpty()){
                throw new RoomNotFoundException("해당하는 방을 찾을 수 없습니다.");
            }
            tmp.get().setStatus(true); //없어진 방 처리
            roomRepository.save(tmp.get());
        }
        myRoomRepository.save(myRoom);
    }

    //방의 참가자들의 정보를 얻는다.(가입되어 있는 사람들)
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
