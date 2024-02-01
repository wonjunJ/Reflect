package com.ssafy.mademe.dto.response;

import com.ssafy.mademe.entity.MBTI;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OMBTICntResponse {
    private MBTI mbti;
    private int cnt;
}
