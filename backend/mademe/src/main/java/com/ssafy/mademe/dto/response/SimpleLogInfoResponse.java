package com.ssafy.mademe.dto.response;

import com.ssafy.mademe.entity.Interest;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleLogInfoResponse {
    private EmotionCntResponse emotion;
    private List<Interest> interests;
}
