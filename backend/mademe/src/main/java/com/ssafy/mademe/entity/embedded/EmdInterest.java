package com.ssafy.mademe.entity.embedded;

import com.ssafy.mademe.entity.InterestCode;
import com.ssafy.mademe.entity.Preference;
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
@Getter @Setter
public class EmdInterest implements Serializable {
    //단방향 관계만
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hobby_code_id")
    private InterestCode interestCode;

    @Enumerated(EnumType.STRING)
    private Preference preference;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmdInterest emdInterest = (EmdInterest) o;
        return Objects.equals(interestCode, emdInterest.interestCode) && Objects.equals(preference, emdInterest.preference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(interestCode, preference);
    }
}
