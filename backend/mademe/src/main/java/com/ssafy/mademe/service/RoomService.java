package com.ssafy.mademe.service;

import com.ssafy.mademe.dto.response.RoomInfoResponse;
import com.ssafy.mademe.entity.HashTag;
import com.ssafy.mademe.entity.Room;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.repository.HashTagRepository;
import com.ssafy.mademe.repository.MyRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final MyRoomRepository myRoomRepository;
    private final HashTagRepository hashTagRepository;

    public List<RoomInfoResponse> getRoomInfos(User user, LocalDate indate, Boolean isExit, Pageable pageable){
        return myRoomRepository.findRoomInfoByUser(user, indate, isExit, pageable);
    }

    public List<String> getHashTagList(Long roomId){
        List<HashTag> list = hashTagRepository.findById_Room_Id(roomId);
        List<String> rst = new ArrayList<>();
        for(HashTag tag : list){
            rst.add(tag.getId().getHashCode().getHashName());
        }
        return rst;
    }
}
