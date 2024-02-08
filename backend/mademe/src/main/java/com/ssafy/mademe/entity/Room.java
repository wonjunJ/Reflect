package com.ssafy.mademe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id", columnDefinition = "INT UNSIGNED")
    private Long id;

    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime createDate;

    private int totalNum;

    private int curNum;

    @Enumerated(EnumType.STRING)
    private RoomType type;

    @Enumerated(EnumType.STRING)
    private RoomMode mode;

    //User테이블 쪽으로 단방향 관계만
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User ownerId;

    private String title;

    private String intro;

    @Lob
    private String thumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private QuestionCode question;

    //0이면 오픈방, 1이면 없어진 방
    @Column(columnDefinition = "TINYINT(1) default 0")
    private boolean status;

    //-------------------------------------------------------------
    @OneToMany(mappedBy = "id.room", cascade = CascadeType.ALL)
    private List<MyRoom> participants = new ArrayList<>();

    @OneToMany(mappedBy = "id.room", cascade = CascadeType.ALL)
    private List<HashTag> hashTags = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createDate = LocalDateTime.now();
    }

    public void addParticipant(MyRoom myRoom){
        participants.add(myRoom);
        myRoom.getId().setRoom(this);
    }

    public void addHashTag(HashTag hashTag){
        hashTags.add(hashTag);
        hashTag.getId().setRoom(this);
    }
}
