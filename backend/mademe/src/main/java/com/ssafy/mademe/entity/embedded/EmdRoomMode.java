package com.ssafy.mademe.entity.embedded;

import com.ssafy.mademe.entity.MBTICode;
import com.ssafy.mademe.entity.PetCode;
import com.ssafy.mademe.entity.Room;
import com.ssafy.mademe.entity.RoomMode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmdRoomMode implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Enumerated(EnumType.STRING)
    private RoomMode roomMode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmdRoomMode emdRoomMode = (EmdRoomMode) o;
        return Objects.equals(room, emdRoomMode.room) && Objects.equals(roomMode, emdRoomMode.roomMode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(room, roomMode);
    }
}
