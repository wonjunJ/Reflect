package com.ssafy.mademe.entity.embedded;

import com.ssafy.mademe.entity.QuestionCode;
import com.ssafy.mademe.entity.TitleCode;
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
public class EmdTitle implements Serializable {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "title_code_id")
    private TitleCode tCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmdTitle emdTitle = (EmdTitle) o;
        return Objects.equals(user, emdTitle.user) && Objects.equals(tCode, emdTitle.tCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, tCode);
    }
}
