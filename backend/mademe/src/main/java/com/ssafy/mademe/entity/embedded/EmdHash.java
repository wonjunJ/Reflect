package com.ssafy.mademe.entity.embedded;

import com.ssafy.mademe.entity.HashCode;
import com.ssafy.mademe.entity.Room;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class EmdHash implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hash_id")
    private HashCode hashCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmdHash emdHash = (EmdHash) o;
        return Objects.equals(room, emdHash.room) && Objects.equals(hashCode, emdHash.hashCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(room, hashCode);
    }
}
