package com.ssafy.mademe.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mademe.utils.JWTUtil;
import com.ssafy.mademe.utils.JwtCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        String token = jwtUtil.resolveToken(request);
        JwtCode code = jwtUtil.validateToken(token);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Map<String, String> errorMessage = new LinkedHashMap<>();

        switch(code){
            case ACCESS:
                errorMessage.put("message", "유저 권한이 부족합니다.");
                errorMessage.put("errorType", "AccessDeniedException");
                break;
            case EXPIRED:
                errorMessage.put("message", "토큰이 만료되었습니다.");
                errorMessage.put("errorType", "TokenExpiredException");
            case DENIED:
                errorMessage.put("message", "토큰이 유효하지 않습니다.");
                errorMessage.put("errorType", "TokenInvalidException");
        }

        errorMessage.put("fieldName","");
        response.getWriter().write(objectMapper.writeValueAsString(errorMessage));
    }
}
