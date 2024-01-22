package com.ssafy.mademe.entity.embedded;

import com.ssafy.mademe.entity.QuestionCode;
import com.ssafy.mademe.entity.Room;
import com.ssafy.mademe.entity.User;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EmdRoom implements Serializable {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmdRoom emdRoom = (EmdRoom) o;
        return Objects.equals(user, emdRoom.user) && Objects.equals(room, emdRoom.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, room);
    }
}
