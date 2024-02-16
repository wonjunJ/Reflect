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
    //이 부분을 Interest로 할 경우 User엔티티와 Diary엔티티에 대해서 추가 쿼리가 발생하며 오류가 난다. 왜지???
    private List<InterestResponse> interests;
}
