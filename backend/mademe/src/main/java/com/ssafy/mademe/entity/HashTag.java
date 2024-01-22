package com.ssafy.mademe.entity;

import com.ssafy.mademe.entity.embedded.EmdHash;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class HashTag {
    @EmbeddedId
    private EmdHash id;
}
