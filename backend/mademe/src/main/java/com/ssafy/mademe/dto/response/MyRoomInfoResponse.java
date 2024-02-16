package com.ssafy.mademe.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.mademe.entity.RoomMode;
import com.ssafy.mademe.entity.RoomType;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyRoomInfoResponse {
    private Long roomId;
    private boolean bmark;
    //참여 날짜
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate inDate;
    //이 방의 생성 일자
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createDate;
    private Integer totalNum;
    private Integer curNum;
    private RoomType type;
    private List<RoomMode> mode;
    private String thumnail;
    private String title;
    private Long qId;
    private List<String> hashTags;
    private boolean isExit;
}
