package com.ssafy.mademe.dto.response;

import com.ssafy.mademe.entity.Gender;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String name;
    private String nickName;
    private String email;
    private String profileImg;
    private String phoneNumber;
    private Gender gender;
    private String birthday;
    private List<String> mbti;
    private String myIntro;
}
