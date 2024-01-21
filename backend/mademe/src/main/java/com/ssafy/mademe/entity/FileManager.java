package com.ssafy.mademe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FileManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_manager_id")
    private Long id;

    //단방향으로만
    @OneToOne
    @JoinColumn(name = "file_info_id")
    private FileInfo fileInfo;

    private String referenceType;

    private Long referenceId;
}
