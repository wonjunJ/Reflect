package com.ssafy.mademe.repository;

import com.ssafy.mademe.dto.response.RoomInfoResponse;
import com.ssafy.mademe.entity.User;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface MyRoomRepositoryCustom {
    List<RoomInfoResponse> findRoomInfoByUser(User user, LocalDate indate, Pageable pageable);
}
