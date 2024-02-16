package com.ssafy.mademe.config.security;

import com.ssafy.mademe.service.UserService;
import com.ssafy.mademe.utils.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.session.JdbcSessionDataSourceScriptDatabaseInitializer;
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
    private final UserService userService;

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
        System.out.println(token);

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
            Long userid = jwtUtil.getUserId(token);
            //여기까지 왔다는 건 토큰이 만료된 것도 아니고 로그아웃 처리가 된것도 아니므로 정상적인 access token이라면 조건문을 넘어간다.
            String value = (String) redisTemplate.opsForValue().get("AT:" + userid);
            log.info(value);
            if(!token.equals(value)){ //정상적인 access token이 아니거나 refresh token이다.
                filterChain.doFilter(request, response);
                return;
            }

            UserDetails userDetails = userService.loadUserByUsername(userid.toString());
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            System.out.println("where.....");
        }
        System.out.println("here???....");
        filterChain.doFilter(request, response);
    }

    private boolean isTokenOnRedisBlackList(String token){
        //토큰을 키값으로 redis에 등록이 되어있는지 체크, 토큰을 키값으로 하는 값이 없다면 로그인 되어있는 상태
        log.info("로그아웃????");
        String value = (String) redisTemplate.opsForValue().get(token);
        return value != null && value.equals("logout");
    }
}
