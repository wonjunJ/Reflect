package com.ssafy.mademe.dto.response;

import com.ssafy.mademe.entity.RoomType;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomInfoResponse {
    private Long roomId;
    private boolean bmark;
    //참여 날짜
    private LocalDate inDate;
    //이 방의 생성 일자
    private LocalDate createDate;
    private RoomType type;
    private String title;
    private Long qId;
    private List<String> hashTags;
    private boolean isExit;
}
