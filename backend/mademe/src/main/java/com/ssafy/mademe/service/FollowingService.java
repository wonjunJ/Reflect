package com.ssafy.mademe.service;

import com.ssafy.mademe.dto.response.FollowingResponse;
import com.ssafy.mademe.entity.Following;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.entity.embedded.EmdFollowing;
import com.ssafy.mademe.exception.user.FailedJoinException;
import com.ssafy.mademe.repository.FollowingRepository;
import com.ssafy.mademe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowingService {

    private final FollowingRepository followingRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<Object, Object> redisTemplate;

    public List<FollowingResponse> getFollowingList(User user, int pgNum){
        List<FollowingResponse> ls = followingRepository.findFollowingListByUser(user, pgNum);
        for(FollowingResponse res : ls){
            //null이 아니라면 상대가 로그인 상태
            res.set_online(redisTemplate.opsForValue().get("AT:" + res.getUserId()) != null);
        }
        return ls;
    }

    public void addFollowing(User user, User follow){
        try {
            //User friend = userRepository.findBySiteId(follow.getSiteId());
            Following following = new Following();
            //following.getId().setUserId(user);
            following.getId().setFollowId(follow); //follow객체에 user의 long타입 id값을 가지고 있다면
            user.addFollowing(following);
            followingRepository.save(following);
        }catch (Exception e){
            throw new FailedJoinException("친구 추가 실패");
        }
    }

    public void delFollowing(User user, User follow){
        User friend = userRepository.findBySiteId(follow.getSiteId());
        EmdFollowing emd = new EmdFollowing(user, friend);
        followingRepository.deleteById(emd);
    }
}
