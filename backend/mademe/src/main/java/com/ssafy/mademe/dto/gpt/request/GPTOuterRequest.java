package com.ssafy.mademe.dto.gpt.request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GPTOuterRequest {
    private DiaryGPTRequest data;
}
