package com.ssafy.mademe.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mademe.utils.ErrorResponse;
import com.ssafy.mademe.utils.JWTUtil;
import com.ssafy.mademe.utils.JwtCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;
    private final JWTUtil jwtUtil;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        String token = jwtUtil.resolveToken(request);
        JwtCode code = jwtUtil.validateToken(token);
        ErrorResponse errorResponse = null;

        switch (code){
            case ACCESS:
                response.setStatus(HttpStatus.FORBIDDEN.value());
                errorResponse = ErrorResponse.builder()
                        .errorType("AccessDeniedException")
                        .message("유저 권한이 부족합니다.")
                        .fieldName("")
                        .build();
                break;
            case EXPIRED:
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                errorResponse = ErrorResponse.builder()
                        .errorType("TokenExpireedException")
                        .message("토큰이 만료되었습니다.")
                        .fieldName("").build();
                break;
            case DENIED:
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                errorResponse = ErrorResponse.builder()
                        .errorType("TokenInvalidException")
                        .message("토큰이 유효하지 않습니다.")
                        .fieldName("").build();
        }

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
