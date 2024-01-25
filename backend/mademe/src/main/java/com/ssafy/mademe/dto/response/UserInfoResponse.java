package com.ssafy.mademe.dto.response;

import com.ssafy.mademe.entity.Gender;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    private String name;
    private String nickName;
    private String email;
    private String profileImg;
    private String phoneNumber;
    private Gender gender;
    private String birthday;
    private String myIntro;
}
