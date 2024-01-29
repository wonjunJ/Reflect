package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.MBTILog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MBTILogRepository extends JpaRepository<MBTILog, Long>, MBTILogRepositoryCustom {

}
