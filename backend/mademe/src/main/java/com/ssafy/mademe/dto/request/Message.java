package com.ssafy.mademe.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Long id;
    private int type; //1: 알림(메시지 포함), 2: 초대(방), 3: 친구추가
    private Long senderId;
    private String senderNick; //보내는 사람의 닉네임
    private Long toId;
    private Long roomId;
    private String msg;
}
