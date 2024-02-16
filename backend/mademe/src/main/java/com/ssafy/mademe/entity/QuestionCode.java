package com.ssafy.mademe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

//단방향으로만 관계 설정
@Entity
@Getter
@Setter
public class QuestionCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id", columnDefinition = "INT UNSIGNED")
    private Long id;

    private String questionName;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Room> rooms;
}
