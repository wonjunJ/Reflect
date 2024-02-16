package com.ssafy.mademe.entity;

import com.ssafy.mademe.entity.embedded.EmdRoom;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class MyRoom {
    @EmbeddedId
    private EmdRoom id;

    //0이면 즐겨찾기 안된것, 1이면 즐겨찾기
    @Column(columnDefinition = "TINYINT(1) default 0")
    private boolean bmark;

    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime inDate;

    //0이면 퇴장 안한 것, 1이면 퇴장한 것
    @Column(columnDefinition = "TINYINT(1) default 0")
    private boolean isExit;

    @PrePersist
    protected void onCreate() {
        inDate = LocalDateTime.now().plusHours(9L);
    }
}
