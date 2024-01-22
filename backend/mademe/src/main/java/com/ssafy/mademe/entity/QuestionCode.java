package com.ssafy.mademe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

//단방향으로만 관계 설정
@Entity
@Getter
@Setter
public class QuestionCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    private String questionName;

    @OneToOne(mappedBy = "question")
    private Room room;
}
