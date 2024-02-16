package com.ssafy.mademe.dto.response;

import com.ssafy.mademe.entity.Diary;
import com.ssafy.mademe.service.DiaryDetailService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collector;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class DiaryDetailResponse {
    private Long diaryId;
    private LocalDate createDate;
    private String content;
    private String diaryImg; // 일기에 포함된 이미지의 URL
    private int diaryScore;
    private String keyword; // 일기와 관련된 키워드
    private String video;  // 비디오 URL

    // 생성자
//    public DiaryDetailResponse(Diary diary) {
//        this.diaryId = diary.getId();
//        this.createDate = diary.getCreateDate().toLocalDate();
//        this.content = diary.getContent();
//        this.diaryImg = diary.getDiaryImg();
//        this.diaryScore = diary.getDiaryScore();
//        this.keyword = diary.getKeyword();
//    }


}
