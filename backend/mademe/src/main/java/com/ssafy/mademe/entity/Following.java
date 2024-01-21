package com.ssafy.mademe.entity;

import com.ssafy.mademe.entity.embedded.EmdFollowing;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Following {
    @EmbeddedId
    private EmdFollowing id;
}
