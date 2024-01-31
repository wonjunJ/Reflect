package com.ssafy.mademe.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmotionCntResponse {
    private Long happiness;
    private Long joy;
    private Long sad;
    private Long anger;
    private Long Disgust;
    private Long none;
}
