package com.ssafy.mademe.entity;

import com.ssafy.mademe.entity.embedded.EmdOMBTI;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity(name = "Ombti_cnt")
@Getter
@Setter
//복합 unique키 설정
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "mbti"})
})
public class OMBTICnt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eval_id")
    private Long id;

    @Embedded
    private EmdOMBTI compositeKey;

    @ColumnDefault("0")
    private int cnt;
}
