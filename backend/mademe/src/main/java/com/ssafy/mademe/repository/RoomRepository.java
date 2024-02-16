package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.Room;
import com.ssafy.mademe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomRepositoryCustom {
    Room findFirstByIdAndOwnerId(Long id, User user);
    @Modifying
    @Query("UPDATE Room e SET e.curNum = e.curNum + 1 WHERE e.id = :id")
    void increaseNumById(@Param("id") Long id); //현재 참가하고 있는 사람의 수를 늘린다.
    @Modifying
    @Query("UPDATE Room e SET e.curNum = e.curNum - 1 WHERE e.id = :id")
    void decreaseNumById(@Param("id") Long id); //현재 참가하고 있는 사람의 수를 늘린다.
}
