package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.RoomModeList;
import com.ssafy.mademe.entity.embedded.EmdRoomMode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomModeListRepository extends JpaRepository<RoomModeList, EmdRoomMode> {
    List<RoomModeList> findById_Room_Id(Long id);
}
