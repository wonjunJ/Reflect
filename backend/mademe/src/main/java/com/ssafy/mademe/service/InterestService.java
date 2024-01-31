package com.ssafy.mademe.service;

import com.ssafy.mademe.entity.Interest;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.repository.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterestService {
    private final InterestRepository interestRepository;

    public List<Interest> getTop5Interest(User user, int top){
        return interestRepository.findInterestList(user, top, null);
    }
}
