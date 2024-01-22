package com.ssafy.mademe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String siteId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String nickName;

    private String pw;

    @Column(nullable = false)
    private String email;

    private int gender;

    @Lob
    private String profileImg;

    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime joinDate;

    private String phoneNumber;

    @ColumnDefault("0")
    private int point;

    //일기 연속 작성 기간
    @ColumnDefault("0")
    private int recordTime;

    private String birthday;

    private String myIntro;

    //회원일 경우 false(0), 탈퇴할 경우 true(1)
    @Column(columnDefinition = "TINYINT(1) default 0")
    private boolean isWithdrawal;

    // ------------------------------------------------------

    @OneToMany(mappedBy = "id.userId")
    private List<Following> followings = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Diary> diaries = new ArrayList<>();

    @OneToMany(mappedBy = "compositeKey.user")
    private List<QuestionRecord> qRecords = new ArrayList<>();

    //한 사용자의 알람 목록
    @OneToMany(mappedBy = "user")
    private List<Alram> alrams = new ArrayList<>();

    @OneToMany(mappedBy = "id.user")
    private List<UserTitle> userTitles = new ArrayList<>();

    @OneToMany(mappedBy = "id.user")
    private List<MyPet> myPets = new ArrayList<>();

    @OneToMany(mappedBy = "id.user")
    private List<MyPetPersonality> personalities = new ArrayList<>();

    //일기에서 분석된 mbti코드별
    @OneToMany(mappedBy = "compositeKey.user")
    private List<MBTICnt> mbtiCodes = new ArrayList<>();

    //상대가 평가한 나의 mbti
    @OneToMany(mappedBy = "compositeKey.user")
    private List<OMBTICnt> mbtiEval = new ArrayList<>();

    @OneToMany(mappedBy = "id.user")
    private List<MyRoom> roomList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Interest> interests = new ArrayList<>();

    //새로운 엔티티가 저장(추가)되기 직전에
    @PrePersist
    protected void onCreate() {
        joinDate = LocalDateTime.now();
    }

}
