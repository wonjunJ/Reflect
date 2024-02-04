package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
