package com.ssafy.mademe.utils;

import com.ssafy.mademe.exception.user.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final JWTUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if(accessor.getCommand() == StompCommand.CONNECT){
//            JwtCode checkToken = jwtUtil.validateToken(accessor.getFirstNativeHeader("Authorization"));
//            if(checkToken != JwtCode.ACCESS){
//                throw new InvalidTokenException("유효한 토큰이 아닙니다.");
//            }
        }

        return message;
    }
}
