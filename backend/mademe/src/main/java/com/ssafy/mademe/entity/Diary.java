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
    @Column(name = "diary_id", columnDefinition = "INT UNSIGNED")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime createDate;

    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime updateDate;

    @Lob
    @Column(columnDefinition = "longtext")
    private String content;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String diaryImg;

    private String video;

    private int diaryScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emotion_code_id")
    private EmotionCode totalEmotion;

    private String keyword;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
    private List<EmotionLog> emotionLogs = new ArrayList<>();
    public void addEmotionLog(EmotionLog emotionLog){
        emotionLogs.add(emotionLog);
        emotionLog.setDiary(this);
    }

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
    private List<MBTILog> mbtiLogs = new ArrayList<>();
    public void addMbtiLog(MBTILog mbtiLog){
        mbtiLogs.add(mbtiLog);
        mbtiLog.setDiary(this);
    }

    //새로운 엔티티가 저장(추가)되기 직전에
    @PrePersist
    protected void onCreate() {
        createDate = LocalDateTime.now().plusHours(9L);
        updateDate = createDate;
    }

    //기존의 엔티티가 업데이트되기 직전에
    @PreUpdate
    protected void onUpdate() {
        updateDate = LocalDateTime.now();
    }

//    public void addEmotionLog(EmotionLog emotionLog){
//        this.getEmotionLogs().add(emotionLog);
//        emotionLog.setDiary(this);
//    }
}
