package com.ssafy.mademe.utils;

import com.ssafy.mademe.exception.user.AlreadyLogoutException;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTUtil {

    private final RedisTemplate<Object, Object> redisTemplate;

    @Value("${jwt.token.secret}")
    private String key;

    private static final Long expireTimeMs = 1000 * 60 * 60L; //1시간 설정
    private static final Long expireTimeMsRefresh = (long) 1000 * 60 * 60 * 24; //1일 설정

    public Long getUserId(String token){ //static 삭제
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
                .getBody().get("userId", Long.class);
    }

    public boolean isExpired(String token){ //static 삭제
        try{
            log.debug("token 값 : {}", token);
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
                    .getBody().getExpiration().before(new Date());
        }catch(ExpiredJwtException | MalformedJwtException e){
            return true;
        }
    }

    public Date getExpiration(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
                .getBody().getExpiration();
    }

    //토큰을 생성한다.
    public String[] createAndGetTokens(Long userId){
        Claims claims = Jwts.claims();
        claims.put("userId", userId);
        //System.out.println(key);

        // Access Token 생성
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact(); //직렬화 처리

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMsRefresh))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        //access 토큰 redis에 등록
        Instant start = new Date().toInstant(); //Date객체를 Duration객체로 만들기 위해 선언
        Instant end = getExpiration(accessToken).toInstant(); //Date객체를 Duration객체로 만들기 위해 선언
        redisTemplate.opsForValue().set("AT:" + userId, accessToken, Duration.between(start, end));

        //refresh 토큰 redis에 등록
        start = new Date().toInstant(); //Date객체를 Duration객체로 만들기 위해 선언
        end = getExpiration(refreshToken).toInstant(); //Date객체를 Duration객체로 만들기 위해 선언
        redisTemplate.opsForValue().set("RT:" + userId, refreshToken, Duration.between(start, end));

        String[] tokens = {accessToken, refreshToken};
        return tokens;
    }

    public void doLogout(Long userId){
        if(redisTemplate.opsForValue().get("AT:"+userId) != null){ //이 유저 아이디로 등록된 access token이 있다.(로그인 상태)
            String token = (String) redisTemplate.opsForValue().get("AT:"+userId);
            Instant start = new Date().toInstant();
            Instant end = getExpiration(token).toInstant();
            redisTemplate.delete("AT:"+userId);
            //해당 access token을 블랙리스트로 등록
            redisTemplate.opsForValue().set(token, "logout", Duration.between(start, end));

            token = (String) redisTemplate.opsForValue().get("RT:"+userId);
            start = new Date().toInstant();
            end = getExpiration(token).toInstant();
            redisTemplate.delete("RT:"+userId);
            //해당 refresh token을 블랙리스트로 등록
            redisTemplate.opsForValue().set(token, "logout", Duration.between(start, end));
        }else {
            log.info("여기왔나??");
            throw new AlreadyLogoutException("이미 로그아웃된 이용자입니다.");
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 로그아웃된 이용자입니다.");
        }
    }

    public String resolveToken(HttpServletRequest request){
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    public JwtCode validateToken(String jwtToken){
        log.info("여기로 왔나??");

        if(jwtToken == null || !jwtToken.startsWith("Bearer ")){
            return JwtCode.DENIED;
        }
        String token = jwtToken.split(" ")[1];

        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return JwtCode.ACCESS;
        }catch(ExpiredJwtException e){
            log.info("만료됐다 토큰...");
            return JwtCode.EXPIRED;
        }catch(JwtException | IllegalArgumentException e){
            log.info("잘못된 jwt 서명입니다.");
        }

        return JwtCode.DENIED;
    }
}
