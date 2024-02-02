package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.MyRoom;
import com.ssafy.mademe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface MyRoomRepository extends JpaRepository<MyRoom, Long>, MyRoomRepositoryCustom {
    Boolean existsById_UserAndInDateBetween(User user, LocalDateTime start, LocalDateTime end);
}
