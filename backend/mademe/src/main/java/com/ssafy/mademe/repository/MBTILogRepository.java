package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.MBTICode;
import com.ssafy.mademe.entity.MBTILog;
import com.ssafy.mademe.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MBTILogRepository extends JpaRepository<MBTILog, Long>, MBTILogRepositoryCustom {
    Page<MBTILog> findByUserAndMbtiCodeOrderByDiaryCreateDateDesc(User user, MBTICode code, Pageable pageable);
}
