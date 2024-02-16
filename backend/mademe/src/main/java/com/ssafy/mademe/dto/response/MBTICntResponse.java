package com.ssafy.mademe.dto.response;

import com.ssafy.mademe.entity.MBTICode;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MBTICntResponse {
    private MBTICode mbtiCode;
    private Long cnt;
    private int percent;
}
