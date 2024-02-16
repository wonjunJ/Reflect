package com.ssafy.mademe.dto.response;

import com.ssafy.mademe.dto.request.Message;
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
    private String mbti;
    private int point;
    private List<String> titles;
    private List<Message> alrams;
    private Long charId;
    private String charName; //원래 캐릭터의 이름
    private String myCharName; //내가 설정한 캐릭터의 이름
    private String myIntro;
    private boolean isOauth;
}
