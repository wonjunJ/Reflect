package com.ssafy.mademe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Alram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alram_id", columnDefinition = "INT UNSIGNED")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    //단방향 관계만
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_id")
    private User from;

    private int type; //1: 알림(메시지 포함), 2: 초대(방), 3: 친구추가

    //안 읽었을 경우 false(0), 읽었을 경우 true(1)
    @Column(columnDefinition = "TINYINT(1) default 0")
    private boolean isRead;

    //단방향 관계만
    //방번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room invitedRoom;
}
