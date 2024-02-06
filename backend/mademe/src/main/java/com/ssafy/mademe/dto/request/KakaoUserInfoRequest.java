package com.ssafy.mademe.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUserInfoRequest {
    private String id;
    private String name;
    private String nickName; //사용자에게 추가로 입력 받는 것
    private String email; //사용자에게 추가로 입력 받는 것
    private String profileImg;
}
