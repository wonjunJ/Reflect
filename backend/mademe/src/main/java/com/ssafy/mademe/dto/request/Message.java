package com.ssafy.mademe.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private int type;
    private Long sender;
    private Long toId;
    private Long roomId;
    private String msg;
}
