package com.ssafy.mademe.repository;

import com.ssafy.mademe.dto.response.FollowingResponse;
import com.ssafy.mademe.dto.response.MyRoomInfoResponse;
import com.ssafy.mademe.entity.Room;
import com.ssafy.mademe.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface MyRoomRepositoryCustom {
    Page<MyRoomInfoResponse> findRoomInfoListByUser(User user, LocalDate indate, Boolean isExit, Pageable pageable);
    List<FollowingResponse> findParticipantsByRoom(Room roomId);
}
