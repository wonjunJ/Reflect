package com.ssafy.mademe.dto.gpt.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryWriteDto {
    @JsonAlias("data")
    private DiaryGPTRequest data;
    @JsonAlias("video")
    private String video;
}
