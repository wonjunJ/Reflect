package com.ssafy.mademe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

//단방향 관계만
@Entity
@Getter
@Setter
public class PetCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id", columnDefinition = "INT UNSIGNED")
    private Long id;

    private String petName;

    private int price;
}
