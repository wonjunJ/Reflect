package com.ssafy.mademe.dto.request;

import com.ssafy.mademe.entity.EmotionCode;
import com.ssafy.mademe.entity.Preference;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddInterestRequest {
    private String name;
    private Preference preference;
    //추가 뒤에 갱신된 정보를 받을 필요가 있으면 필요
    //private int top;
}
