package com.ssafy.mademe.dto.response;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowInfoResponse {
    private String nickName;
    private String profileImg;
    private String mbti;
    private List<String> interests;
    private boolean is_follow;
}
