package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.Room;
import com.ssafy.mademe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findFirstByIdAndOwnerId(Long id, User user);
}
