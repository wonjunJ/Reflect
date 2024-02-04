package com.ssafy.mademe.controller;

import com.ssafy.mademe.dto.response.*;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.service.DiaryService;
import com.ssafy.mademe.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;
    private final RoomService roomService;

    //이 달의 총괄 상태
    @GetMapping("/")
    public List<MonthDiaryResponse> getMonthDiaryStatus(@AuthenticationPrincipal User user){
        return diaryService.getMonthDiaryStatus(user);
    }

    //간단 데일리 정보 얻기
    @GetMapping("/simple")
    public Map<String, Object> getSimpleDailyInfo(@AuthenticationPrincipal User user, @RequestParam("diaryId") Long diaryId,
                                                  Pageable pageable){
        Map<String, Object> json = new HashMap<>();
        SimpleDiaryResponse dInfos = diaryService.getSimpleDiaryInfo(diaryId);
        json.put("diary", dInfos);
        Page<MyRoomInfoResponse> data = roomService.getRoomInfos(user, dInfos.getDate(), null, pageable);
        json.put("community", data.get().collect(Collectors.toList()));
        json.put("communityPagingInfo", PagingInfoResponse.builder()
                .totalPage(data.getTotalPages()) //전체 페이지 수
                .pageNum(data.getNumber()) //현재 페이지 번호
                .isFirst(data.isFirst()) //이 페이지가 첫번째 페이지 인지
                .hasNext(data.hasNext()) //다음 페이지가 존재하는지
                .build()
        );
        return json;
    }
}
