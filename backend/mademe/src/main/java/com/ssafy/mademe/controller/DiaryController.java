package com.ssafy.mademe.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mademe.dto.gpt.request.DiaryGPTRequest;
import com.ssafy.mademe.dto.gpt.request.DiaryWriteDto;
import com.ssafy.mademe.dto.response.*;
import com.ssafy.mademe.entity.Diary;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.repository.DiaryRepository;
import com.ssafy.mademe.service.DiaryService;
import com.ssafy.mademe.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
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
    private final DiaryRepository diaryRepository;

    //이 달의 총괄 상태
    @GetMapping("")
    public List<MonthDiaryResponse> getMonthDiaryStatus(@AuthenticationPrincipal User user,
                                                        @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                        @RequestParam("date")
                                                        LocalDate date){
        System.out.println("날짜는 " + date);
        return diaryService.getMonthDiaryStatus(user,date);
    }

    //간단 데일리 정보 얻기
    @GetMapping("/simple")
    public Map<String, Object> getSimpleDailyInfo(@AuthenticationPrincipal User user, @RequestParam("diaryId") Long diaryId,
                                                  @PageableDefault(size = 4) Pageable pageable){
        Map<String, Object> json = new HashMap<>();
        SimpleDiaryResponse dInfos = diaryService.getSimpleDiaryInfo(diaryId);
        json.put("diary", dInfos);
        Page<MyRoomInfoResponse> data = roomService.getRoomInfos(user, dInfos.getDate(), null, pageable);
        for(MyRoomInfoResponse tmp : data){
            tmp.setHashTags(roomService.getHashTagList(tmp.getRoomId()));
        }
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

    //일기 작성(GPT 분석 데이터까지 함께 저장)
    @PostMapping("/gpt")
    public Map<String, Long> addDiaryGPTLog(@AuthenticationPrincipal User user, @RequestBody DiaryWriteDto dto) {
        DiaryGPTRequest request = dto.getData();
        //request.getDailyData().getKeywords().forEach(s -> System.out.println("data는 " + s));
        Diary diaryId = diaryService.addDiaryGPTLog(user, request);
        //비디오 url 저장
        String videoUrl = dto.getVideo();
        diaryId.setVideo(videoUrl);
        diaryRepository.save(diaryId);
        //칭호 획득 관련 처리 추가
        Map<String, Long> state = new HashMap<>();
        //state.put("state", "success");
        state.put("diaryId", diaryId.getId());
        return state;
    }

    @PostMapping("/img")
    public Map<String, String> addDiaryImg(@RequestBody Diary diary){ //id값하고 diaryImg값만 받아온다.
        diaryService.addDiaryImg(diary);
        Map<String, String> state = new HashMap<>();
        //state.put("state", "success");
        state.put("state", "success");
        return state;
    }
}
