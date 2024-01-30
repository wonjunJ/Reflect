package com.ssafy.mademe.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmotionCntResponse {
    private int happiness;
    private int joy;
    private int sad;
    private int anger;
    private int Disgust;
    private int none;
}
