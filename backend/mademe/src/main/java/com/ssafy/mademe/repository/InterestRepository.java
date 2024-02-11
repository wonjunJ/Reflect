package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.Interest;
import com.ssafy.mademe.entity.Preference;
import com.ssafy.mademe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Long>, InterestRepositoryCustom {
    List<Interest> findTop5ByUserAndCompositeKey_PreferenceOrderByCntDesc(User user, Preference prf);
    Interest findFirstByUserAndCompositeKey_PreferenceAndCompositeKey_InterestCode_HobbyName(User user, Preference pf, String name);
    @Modifying
    @Query("UPDATE Interest e SET e.cnt = e.cnt + 1 WHERE e.id = :id")
    void increaseCntById(@Param("id") Long id);
    Integer countByUserAndCompositeKey_Preference(User user, Preference pf);
}
