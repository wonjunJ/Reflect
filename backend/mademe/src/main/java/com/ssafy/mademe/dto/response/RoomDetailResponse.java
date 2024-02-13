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
public class RoomDetailResponse {
    private Long roomId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createDate;
    private RoomType type;
    private List<RoomMode> mode;
    private Long ownerId;
    private String title;
    private String intro;
    private String thumbnail;
    private Long qId; //백문백답 방이면 해당하는 질문의 ID
    private List<FollowingResponse> participants; //현재 이 방에 가입되어 있는 사람들의 리스트
    private List<String> hashTags;
}
