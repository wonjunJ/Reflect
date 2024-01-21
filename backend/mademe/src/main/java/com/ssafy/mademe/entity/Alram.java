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
    @Column(name = "alram_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    //단방향 관계만
    @OneToOne
    @JoinColumn(name = "from_id")
    private User from;

    private int type; //0: 맞팔, 1: 초대, 2: 일반

    //안 읽었을 경우 false(0), 읽었을 경우 true(1)
    @Column(columnDefinition = "TINYINT(1) default 0")
    private boolean isRead;

    //단방향 관계만
    //방번호
    @OneToOne
    @JoinColumn(name = "room_id")
    private Room invitedRoom;
}
