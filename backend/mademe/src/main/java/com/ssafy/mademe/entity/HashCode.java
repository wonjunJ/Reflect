package com.ssafy.mademe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class HashCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hash_id")
    private Long id;

    @Column(unique = true)
    private String hashName;

    @OneToMany(mappedBy = "id.hashCode")
    private List<HashTag> hashTagRooms = new ArrayList<>();
}
