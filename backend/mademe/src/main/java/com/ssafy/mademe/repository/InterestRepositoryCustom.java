package com.ssafy.mademe.repository;

import com.ssafy.mademe.dto.response.InterestLsResponse;
import com.ssafy.mademe.entity.Interest;
import com.ssafy.mademe.entity.Preference;
import com.ssafy.mademe.entity.User;

import java.util.List;

public interface InterestRepositoryCustom {
    List<Interest> findInterestList(User user, int top, Preference prf);
}
