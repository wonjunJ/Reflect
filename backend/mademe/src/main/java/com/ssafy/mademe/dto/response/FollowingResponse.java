package com.ssafy.mademe.dto.response;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class FollowingResponse {
    private final Long userId;
    private final String profileImg;
    private final String nickName;
    private List<InterestResponse> interests;
    private List<String> titles; //칭호
    private String mbti;
    private String followIntro;
    //private final int alike; //유사도
    private boolean is_follow;
    private boolean is_online;
}
