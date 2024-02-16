package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.HashTag;
import com.ssafy.mademe.entity.Room;
import com.ssafy.mademe.entity.embedded.EmdHash;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HashTagRepository extends JpaRepository<HashTag, EmdHash> {
    List<HashTag> findById_Room_Id(Long id);
}
