package com.ssafy.mademe.dto.response;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleLogInfoResponse {
    private EmotionCntResponse emotion;
    private List<String> interests;
}
