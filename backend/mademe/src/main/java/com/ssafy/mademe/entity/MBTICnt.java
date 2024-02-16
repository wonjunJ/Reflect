//package com.ssafy.mademe.entity;
//
//import com.ssafy.mademe.entity.embedded.EmdMBTI;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//import org.hibernate.annotations.ColumnDefault;
//
//@Entity(name = "mbti_cnt")
//@Getter
//@Setter
////복합 unique키 설정
//@Table(uniqueConstraints = {
//        @UniqueConstraint(columnNames = {"user_id", "mbti_code"})
//})
//public class MBTICnt {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "mbti_cnt_id")
//    private Long id;
//
//    @Embedded
//    private EmdMBTI compositeKey;
//
//    @ColumnDefault("0")
//    private int cnt;
//}
