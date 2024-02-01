package com.ssafy.mademe.repository;

import com.ssafy.mademe.dto.response.InterestLsResponse;
import com.ssafy.mademe.dto.response.InterestResponse;
import com.ssafy.mademe.entity.Interest;
import com.ssafy.mademe.entity.Preference;
import com.ssafy.mademe.entity.User;

import java.util.List;

public interface InterestRepositoryCustom {
    List<InterestResponse> findInterestList(User user, int top, Preference prf);
    Integer findFourthCount(User user, Preference prf);
}
