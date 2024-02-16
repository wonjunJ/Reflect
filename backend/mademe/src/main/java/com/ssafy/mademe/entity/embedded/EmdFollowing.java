package com.ssafy.mademe.entity.embedded;

import com.ssafy.mademe.entity.User;
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
public class EmdFollowing implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    //일단은 단방향으로만
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follow_id")
    private User followId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmdFollowing emdfollowing = (EmdFollowing) o;
        return Objects.equals(userId, emdfollowing.userId) && Objects.equals(followId, emdfollowing.followId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, followId);
    }
}
