package com.ssafy.mademe.dto.response;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterestLsResponse {
    private List<String> LIKE;
    private List<String> UNLIKE;
}
