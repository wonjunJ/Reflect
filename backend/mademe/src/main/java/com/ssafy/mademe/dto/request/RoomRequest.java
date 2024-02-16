package com.ssafy.mademe.dto.request;

import com.ssafy.mademe.entity.RoomMode;
import com.ssafy.mademe.entity.RoomType;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {
    private int totalNum;
    private RoomType type;
    private List<RoomMode> mode;
    private String title;
    private String intro;
    private String thumbnail;
    private Long questionId;
    private List<String> hashTags;
}
