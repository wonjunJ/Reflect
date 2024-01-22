package com.ssafy.mademe.entity;

import com.ssafy.mademe.entity.embedded.EmdPersonality;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MyPetPersonality {
    @EmbeddedId
    private EmdPersonality id;

    //비선택됐을 경우 false(0), 선택됐을 경우 true(1)
    @Column(columnDefinition = "TINYINT(1) default 0")
    private boolean selected;
}
