package com.ssafy.mademe.entity;

import com.ssafy.mademe.entity.embedded.EmdQuestion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "question_id"})
})
public class QuestionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qrec_id")
    private Long id;

    @Embedded
    private EmdQuestion compositeKey;

    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime createDate;

    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime updateDate;

    @Lob
    private String answer;

    @PrePersist
    protected void onCreate() {
        createDate = LocalDateTime.now();
        updateDate = createDate;
    }

    @PreUpdate
    protected void onUpdate() {
        updateDate = LocalDateTime.now();
    }
}
