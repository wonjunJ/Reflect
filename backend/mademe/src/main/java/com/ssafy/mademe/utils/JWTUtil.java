package com.ssafy.mademe.utils;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
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

    public String getUserId(String token){ //static 삭제
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
                .getBody().get("userId", String.class);
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

    public String createAndGetToken(String userId){
        Claims claims = Jwts.claims();
        claims.put("userId", userId);
        //System.out.println(key);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact(); //직렬화 처리

        Instant start = new Date().toInstant(); //Date객체를 Duration객체로 만들기 위해 선언
        Instant end = getExpiration(token).toInstant(); //Date객체를 Duration객체로 만들기 위해 선언
        redisTemplate.opsForValue().set("RT:" + userId, token, Duration.between(start, end));
        return token;
    }

    public void doLogout(String userId){
        if(redisTemplate.opsForValue().get("RT:"+userId) != null){
            String token = (String) redisTemplate.opsForValue().get("RT:"+userId);
            Instant start = new Date().toInstant();
            Instant end = getExpiration(token).toInstant();
            redisTemplate.delete("RT:"+userId);
            redisTemplate.opsForValue().set(token, "logout", Duration.between(start, end));
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 로그아웃된 이용자입니다.");
        }
    }
}
