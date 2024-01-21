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
    @Column(name = "room_id")
    private Long id;

    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime createDate;

    private int totalNum;

    private int curNum;

    @Enumerated(EnumType.STRING)
    private RoomType type;

    //User테이블 쪽으로 단방향 관계만
    @OneToOne
    @JoinColumn(name = "user_id")
    private User ownerId;

    private String title;

    private String intro;

    @Lob
    private String thumbnail;

    @OneToOne
    @JoinColumn(name = "question_id")
    private QuestionCode question;

    @OneToMany(mappedBy = "id.room")
    private List<MyRoom> participants = new ArrayList<>();

    @OneToMany(mappedBy = "id.room")
    private List<HashTag> hashTags = new ArrayList<>();
}
