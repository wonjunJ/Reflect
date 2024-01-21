package com.ssafy.mademe.entity;

import com.ssafy.mademe.entity.embedded.EmdInterest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"hobby_code_id", "preference"})
})
public class Interest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Embedded
    private EmdInterest compositeKey;

    @ColumnDefault("0")
    private int cnt;
}
