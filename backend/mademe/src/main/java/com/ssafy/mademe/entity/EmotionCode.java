package com.ssafy.mademe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class EmotionCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emotion_code_id", columnDefinition = "INT UNSIGNED")
    private Long id;

    private String emotionName;

    @OneToMany(mappedBy = "emotionCode", cascade = CascadeType.ALL)
    private List<EmotionLog> emotionLogs = new ArrayList<>();

    @OneToMany(mappedBy = "totalEmotion", cascade = CascadeType.ALL)
    private List<Diary> diaries = new ArrayList<>();
}
