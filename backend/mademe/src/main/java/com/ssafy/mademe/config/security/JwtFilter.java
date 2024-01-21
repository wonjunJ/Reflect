package com.ssafy.mademe.config.security;

import com.ssafy.mademe.utils.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

//    @Value("${jwt.token.secret}")
//    private String key;

    private final JWTUtil jwtUtil;
    private final RedisTemplate<Object, Object> redisTemplate;

    //먼저 여기로 요청이 들어온 후에 SecurityConfig에 있는 권한 정보를 확인하러 간다.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        //System.out.println(authorization);


        if(authorization == null || !authorization.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];

        if(token.isEmpty()){
            log.warn("bearer 뒤에 값이 없습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        if(jwtUtil.isExpired(token)){
            log.warn("해당 토큰은 만료된 토큰입니다.");
            filterChain.doFilter(request, response);
            return;
        }

        if (!isTokenOnRedisBlackList(token)){ //로그아웃이 되지 않았다.
//            UserDetails userDetails = userService.loadUserByUsername(jwtUtil.getUserId(token));
//
//            UsernamePasswordAuthenticationToken authenticationToken =
//                    new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//
//            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }

    private boolean isTokenOnRedisBlackList(String token){
        String value = (String) redisTemplate.opsForValue().get(token);
        return value != null && value.equals("logout");
    }
}
