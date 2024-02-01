package com.ssafy.mademe.dto.response;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyMBTIDetailResponse {
    private String mbti;
    private List<MBTICntResponse> mbtiInfo;
}
