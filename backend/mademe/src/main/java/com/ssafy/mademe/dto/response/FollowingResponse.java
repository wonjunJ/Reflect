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
    private boolean is_online;
}
