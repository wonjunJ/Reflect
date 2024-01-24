package com.ssafy.mademe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

//단방향 관계만
@Entity
@Getter
@Setter
public class TitleCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "title_code_id", columnDefinition = "INT UNSIGNED")
    private Long id;

    private String titleName;
}
