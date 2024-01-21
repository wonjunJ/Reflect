package com.ssafy.mademe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class EmotionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emotion_log_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "emotion_code_id")
    private EmotionCode emotionCode;

    @Lob
    private String sentence;

    @ManyToOne
    @JoinColumn(name = "diary_id")
    private Diary diary;
}
