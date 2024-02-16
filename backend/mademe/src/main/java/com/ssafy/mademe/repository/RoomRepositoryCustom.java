package com.ssafy.mademe.repository;

import com.ssafy.mademe.dto.response.InterestResponse;
import com.ssafy.mademe.dto.response.RoomDetailResponse;
import com.ssafy.mademe.dto.response.RoomListResponse;
import com.ssafy.mademe.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoomRepositoryCustom {
    Page<RoomListResponse> findTotalRoomList(List<String> filter, String searchWord, int from, int to, Pageable pageable);
    List<RoomDetailResponse> findRecommendedRoom(List<InterestResponse> myInterests);
    List<RoomDetailResponse> findMoreRecommend(List<Long> ids, int limit);
}
