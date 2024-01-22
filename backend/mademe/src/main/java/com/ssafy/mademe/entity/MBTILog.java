package com.ssafy.mademe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MBTILog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mbti_log_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @Enumerated(EnumType.STRING)
    private MBTICode mbtiCode;

    @Lob
    private String sentence;
}
