package com.ssafy.mademe.entity;

import com.ssafy.mademe.entity.embedded.EmdRoom;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MyRoom {
    @EmbeddedId
    private EmdRoom id;

    //0이면 즐겨찾기 안된것, 1이면 즐겨찾기
    @Column(columnDefinition = "TINYINT(1) default 0")
    private boolean bmark;
}
