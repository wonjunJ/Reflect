package com.ssafy.mademe.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class FollowingResponse {
    private final Long userId;
    private final String profileImg;
    private final String nickName;
    //private final int alike; //유사도
    private boolean is_online;
}
