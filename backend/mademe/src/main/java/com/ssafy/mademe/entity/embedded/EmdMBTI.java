package com.ssafy.mademe.entity.embedded;

import com.ssafy.mademe.entity.MBTICode;
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
public class EmdMBTI implements Serializable {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private MBTICode mbtiCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmdMBTI emdMBTI = (EmdMBTI) o;
        return Objects.equals(user, emdMBTI.user) && Objects.equals(mbtiCode, emdMBTI.mbtiCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, mbtiCode);
    }
}
