package com.ssafy.mademe.entity;

import com.ssafy.mademe.entity.embedded.EmdPet;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MyPet {
    @EmbeddedId
    private EmdPet id;

    private String mypetName;
}
