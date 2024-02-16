package com.ssafy.mademe.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeOwnerRequest {
    private Long roomId;
    private Long ownerId;
}
