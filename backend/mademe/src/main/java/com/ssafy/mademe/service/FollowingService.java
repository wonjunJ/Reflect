package com.ssafy.mademe.service;

import com.ssafy.mademe.dto.request.Message;
import com.ssafy.mademe.dto.response.FollowingResponse;
import com.ssafy.mademe.dto.response.InterestResponse;
import com.ssafy.mademe.dto.response.UserInfoResponse;
import com.ssafy.mademe.entity.Following;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.entity.embedded.EmdFollowing;
import com.ssafy.mademe.exception.user.FailedJoinException;
import com.ssafy.mademe.exception.user.UserNotFoundException;
import com.ssafy.mademe.repository.FollowingRepository;
import com.ssafy.mademe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowingService {

    private final FollowingRepository followingRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final InterestService interestService;
    private final RedisTemplate<Object, Object> redisTemplate;
    private final SimpMessageSendingOperations messageTemplate;
    private final AlramService alramService;

    public Page<FollowingResponse> getFollowingList(User user, Pageable pageable){
        Page<FollowingResponse> ls = followingRepository.findFollowingListByUser(user, pageable);
        for(FollowingResponse res : ls){
            res.set_follow(true);
            //null이 아니라면 상대가 로그인 상태
            res.set_online(redisTemplate.opsForValue().get("AT:" + res.getUserId()) != null);
        }
        return ls;
    }

    public FollowingResponse getFollowingInfo(User user, Long id){
        Optional<User> follow = userRepository.findById(id);
        if(follow.isPresent()){
            String mbtiStr = userService.getUserMbtiInfo(follow.get());
            List<String> titleList = userService.getTitles(follow.get()); //정보가 없다면 빈 리스트가 리턴
            List<InterestResponse> inter = interestService.getTop5Interest(follow.get());
            return mapToFollowingInfo(user, follow.get(), mbtiStr, inter, titleList);
        }
        throw new UserNotFoundException("일치하는 회원을 찾을 수 없습니다.");
    }

    public void addFollowing(User user, User follow){
        try {
            User fId = userRepository.findFirstByNickName(follow.getNickName());
            if (fId == null) {
                throw new UserNotFoundException("유저의 이름이 null 입니다.");
            }
            Following following = new Following();
//            following.getId().setFollowId(follow); //follow객체에 user의 long타입 id값을 가지고 있다면
//            following.getId().setUserId(user);
            EmdFollowing emd = new EmdFollowing(user, fId);
            following.setId(emd);
            //user.addFollowing(following);
            followingRepository.save(following);

            //친구 추가시 상대방에게 알림
            Message msg = alramService.manageMsg( //메시지를 Alram 테이블에 저장
                    Message.builder()
                            .type(3)
                            .senderId(user.getId())
                            .senderNick(user.getNickName())
                            .toId(fId.getId())
                            .msg("상대방이 나를 친구 추가 하였습니다.")
                            .build(),
                    user
            );
            messageTemplate.convertAndSend("/alram/msg-to/" + fId.getId(), msg);
        }catch (Exception e){
            throw new FailedJoinException("친구 추가 실패");
        }
    }

    public void delFollowing(User user, User follow){
        //User friend = userRepository.findBySiteId(follow.getSiteId());
        EmdFollowing emd = new EmdFollowing(user, follow); ////follow객체에 user의 long타입 id값을 가지고 있다면
        followingRepository.deleteById(emd);
    }

    private FollowingResponse mapToFollowingInfo(User user, User follow, String mbti,
                                                 List<InterestResponse> interests, List<String> titles){
        return FollowingResponse.builder()
                .userId(follow.getId())
                .profileImg(follow.getProfileImg())
                .nickName(follow.getNickName())
                .interests(interests)
                .titles(titles)
                .mbti(mbti)
                .followIntro(follow.getMyIntro())
                .is_follow(followingRepository.existsById_UserIdAndId_FollowId(user, follow))
                .is_online(redisTemplate.opsForValue().get("AT:" + follow.getId()) != null)
                .build();
    }
}
