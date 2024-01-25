package com.ssafy.mademe.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckCodeResponse {
    private String email;
    private String code;
    private String msg;
}
