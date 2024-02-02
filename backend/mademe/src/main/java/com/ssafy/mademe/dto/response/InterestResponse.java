package com.ssafy.mademe.dto.response;

import com.ssafy.mademe.entity.Preference;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterestResponse {
    private Long interestId;
    private String interestName;
    private Preference preference;
    private int cnt;
}
