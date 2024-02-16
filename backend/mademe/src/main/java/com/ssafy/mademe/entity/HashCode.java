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
    @Column(name = "hash_id", columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(unique = true)
    private String hashName;

    @OneToMany(mappedBy = "id.hashCode", cascade = CascadeType.ALL)
    private List<HashTag> hashTagRooms = new ArrayList<>();

    public void addHashTag(HashTag hashTag){
        this.getHashTagRooms().add(hashTag);
        hashTag.getId().setHashCode(this);
    }
}
