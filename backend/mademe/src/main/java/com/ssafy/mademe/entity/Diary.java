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
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime createDate;

    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime updateDate;

    @Lob
    private String content;

    @Lob
    private String diaryImg;

    private int diaryScore;

    @ManyToOne
    @JoinColumn(name = "emotion_code_id")
    private EmotionCode totalEmotion;

    private String keyword;

    @OneToMany(mappedBy = "diary")
    private List<EmotionLog> emotionLogs = new ArrayList<>();

    //새로운 엔티티가 저장(추가)되기 직전에
    @PrePersist
    protected void onCreate() {
        createDate = LocalDateTime.now();
        updateDate = createDate;
    }

    //기존의 엔티티가 업데이트되기 직전에
    @PreUpdate
    protected void onUpdate() {
        updateDate = LocalDateTime.now();
    }
}
