package com.ssafy.mademe.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyPetInfoResponse {
    private Long petId;
    private String petName;
    private String mypetName;
    private int price;
}
