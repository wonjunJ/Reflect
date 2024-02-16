package com.ssafy.mademe.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckCodeRequest {
    private String user_id;
    private String email;
    private String code;
    private String pw; //변경할 패스워드
}
