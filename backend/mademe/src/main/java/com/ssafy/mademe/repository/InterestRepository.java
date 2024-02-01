package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.Interest;
import com.ssafy.mademe.entity.Preference;
import com.ssafy.mademe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Long>, InterestRepositoryCustom {
    List<Interest> findTop5ByUserAndCompositeKey_PreferenceOrderByCntDesc(User user, Preference prf);
    Interest findFirstByUserAndCompositeKey_PreferenceAndCompositeKey_InterestCode_HobbyName(User user, Preference pf, String name);
}
