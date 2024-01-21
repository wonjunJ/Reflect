package com.ssafy.mademe.entity.embedded;

import com.ssafy.mademe.entity.MBTI;
import com.ssafy.mademe.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EmdOMBTI implements Serializable {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private MBTI mbti;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmdOMBTI emdOMBTI = (EmdOMBTI) o;
        return Objects.equals(user, emdOMBTI.user) && Objects.equals(mbti, emdOMBTI.mbti);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, mbti);
    }
}
