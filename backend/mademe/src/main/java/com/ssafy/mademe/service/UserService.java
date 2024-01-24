package com.ssafy.mademe.service;

import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.repository.UserRepository;
import com.ssafy.mademe.utils.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final RedisTemplate<Object, Object> redisTemplate;
    private final JWTUtil jwtUtil;

    public Map<String, String> doLogin(String userId, String userPw){
        User user = userRepository.findBySiteId(userId);
        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "아이디 또는 비밀번호 확인 후 다시 로그인하세요!");
            //throw new UserNotFoundException("아이디 또는 비밀번호 확인 후 다시 로그인하세요!");
        }
        if(!encoder.matches(userPw, user.getPw())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "비밀번호가 올바르지 않습니다.");
            //throw new InvalidPasswordException("비밀번호가 올바르지 않습니다.");
        }
        String[] tokens = jwtUtil.createAndGetTokens(user.getId());
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("AccessToken", tokens[0]);
        tokenMap.put("RefreshToken", tokens[1]);
        return tokenMap;
    }

    public void joinMember(User user) {
        try {
            String password = user.getPw();
            user.setPw(encoder.encode(password));
            userRepository.save(user);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "회원가입에 실패하였습니다.");
        }
    }

    public boolean idCheck(String userId) {
        return userRepository.existsBySiteId(userId);
    }

    public boolean nickCheck(String nickName) {
        return userRepository.existsByNickName(nickName);
    }

    public Map<String, String> reissuedToken(HttpServletRequest request){
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];
        Long userid = jwtUtil.getUserId(token);
        //정상적인 refresh 토큰이다
        if(token.equals(redisTemplate.opsForValue().get("RT:" + userid))){
            String[] tokens = jwtUtil.createAndGetTokens(userid);
            Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put("AccessToken", tokens[0]);
            tokenMap.put("RefreshToken", tokens[1]);
            return tokenMap;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "올바른 refresh token이 아닙니다.");
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        System.out.println(userId);
        Optional<User> foundUser = userRepository.findById(Long.valueOf(userId));
        System.out.println(foundUser.get().getName());

        if(foundUser.isPresent()){
            return foundUser.get();
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호를 찾을 수 없습니다.");
    }
}
