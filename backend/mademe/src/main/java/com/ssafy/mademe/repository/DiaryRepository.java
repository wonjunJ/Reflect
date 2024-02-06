package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.Diary;
import com.ssafy.mademe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long>, DiaryRepositoryCustom {
    List<Diary> findByUserAndCreateDateAfter(User user, LocalDateTime dateTime);
    Boolean existsByUserAndCreateDateBetween(User user, LocalDateTime start, LocalDateTime end);
}
