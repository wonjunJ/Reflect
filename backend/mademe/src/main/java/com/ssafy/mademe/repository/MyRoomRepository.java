package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.MyRoom;
import com.ssafy.mademe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface MyRoomRepository extends JpaRepository<MyRoom, Long> {
    Boolean existsById_UserAndInDate(User user, LocalDate dateTime);
}
