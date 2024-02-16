package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.MyRoom;
import com.ssafy.mademe.entity.Room;
import com.ssafy.mademe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MyRoomRepository extends JpaRepository<MyRoom, Long>, MyRoomRepositoryCustom {
    Boolean existsById_UserAndInDateBetween(User user, LocalDateTime start, LocalDateTime end);
    List<MyRoom> findById_Room(Room roomId);
    MyRoom findFirstById_UserAndId_Room(User user, Room room);
    Integer countById_RoomAndIsExitIsFalse(Room room);
}
