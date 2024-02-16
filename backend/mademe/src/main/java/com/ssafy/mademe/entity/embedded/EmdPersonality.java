package com.ssafy.mademe.entity.embedded;

import com.ssafy.mademe.entity.PetCode;
import com.ssafy.mademe.entity.PetPersonality;
import com.ssafy.mademe.entity.User;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
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
public class EmdPersonality implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "per_id")
    private PetPersonality petPersonality;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmdPersonality emdPersonality = (EmdPersonality) o;
        return Objects.equals(user, emdPersonality.user) && Objects.equals(petPersonality, emdPersonality.petPersonality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, petPersonality);
    }
}
