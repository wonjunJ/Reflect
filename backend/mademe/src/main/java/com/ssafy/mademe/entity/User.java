package com.ssafy.mademe.entity;

import com.ssafy.mademe.utils.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", columnDefinition = "INT UNSIGNED")
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

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String profileImg;

    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime joinDate;

    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime checkDate; //마지막 출석 체크한 날짜

    private String phoneNumber;

    @ColumnDefault("0")
    private int point;

    //일기 연속 작성 기간
    @ColumnDefault("0")
    private int recordTime;

    private String birthday;

    private String myIntro;

    //소셜 로그인을 하였을 경우 true(1), 자체 로그인을 하였을 경우 false(0)
    @Column(columnDefinition = "TINYINT(1) default 0")
    private boolean isOauth;

    //회원일 경우 false(0), 탈퇴할 경우 true(1)
    @Column(columnDefinition = "TINYINT(1) default 0")
    private boolean isWithdrawal;

    // ------------------------------------------------------

    @OneToMany(mappedBy = "id.userId", cascade = CascadeType.ALL)
    private List<Following> followings = new ArrayList<>();
    public void addFollowing(Following following){
        followings.add(following);
        following.getId().setUserId(this);
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Diary> diaries = new ArrayList<>();
    public void addDiary(Diary diary){
        diaries.add(diary);
        diary.setUser(this);
    }

    @OneToMany(mappedBy = "compositeKey.user", cascade = CascadeType.ALL)
    private List<QuestionRecord> qRecords = new ArrayList<>();
    public void addQrecord(QuestionRecord qrecord){
        qRecords.add(qrecord);
        qrecord.getCompositeKey().setUser(this);
    }

    //한 사용자의 알람 목록
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Alram> alrams = new ArrayList<>();
    public void addAlram(Alram alram){
        alrams.add(alram);
        alram.setUser(this);
    }

    @OneToMany(mappedBy = "id.user", cascade = CascadeType.ALL)
    private List<UserTitle> userTitles = new ArrayList<>();
    public void addUserTitle(UserTitle userTitle){
        userTitles.add(userTitle);
        userTitle.getId().setUser(this);
    }

    @OneToMany(mappedBy = "id.user", cascade = CascadeType.ALL)
    private List<MyPet> myPets = new ArrayList<>();

    @OneToMany(mappedBy = "id.user", cascade = CascadeType.ALL)
    private List<MyPetPersonality> personalities = new ArrayList<>();

//    //일기에서 분석된 mbti코드별
//    @OneToMany(mappedBy = "compositeKey.user", cascade = CascadeType.ALL)
//    private List<MBTICnt> mbtiCodes = new ArrayList<>();

    //상대가 평가한 나의 mbti
    @OneToMany(mappedBy = "compositeKey.user", cascade = CascadeType.ALL)
    private List<OMBTICnt> mbtiEval = new ArrayList<>();

    @OneToMany(mappedBy = "id.user", cascade = CascadeType.ALL)
    private List<MyRoom> roomList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Interest> interests = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<MBTILog> mbtiLogs = new ArrayList<>();
    public void addMbtiLog(MBTILog mbtiLog){
        mbtiLogs.add(mbtiLog);
        mbtiLog.setUser(this);
    }

    //새로운 엔티티가 저장(추가)되기 직전에
    @PrePersist
    protected void onCreate() {
        joinDate = LocalDateTime.now().plusHours(9L);
    }

    //------------------------------------------------------------------
    @Transient
    private Set<UserRole> roles = new HashSet<>(Arrays.asList(UserRole.USER)); //user권한으로 초기값 지정

    @Transient
    private String saveId;

    //UserDetails 관련 오버라이딩
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getPassword() {
        return this.pw;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
