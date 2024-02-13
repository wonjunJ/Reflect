package com.ssafy.mademe.entity;

import com.ssafy.mademe.entity.embedded.EmdRoomMode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RoomModeList {
    @EmbeddedId
    private EmdRoomMode id;
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "mode_ls_id", columnDefinition = "INT UNSIGNED")
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "room_id")
//    private Room room;
//
//    @Enumerated(EnumType.STRING)
//    private RoomMode roomMode;
}
