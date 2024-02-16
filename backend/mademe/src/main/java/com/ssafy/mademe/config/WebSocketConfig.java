package com.ssafy.mademe.config;

import com.ssafy.mademe.utils.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final StompHandler stompHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        config.setApplicationDestinationPrefixes("/app"); // 초대 버튼을 눌렀을 때, prefix 로 붙는 부분
        // 만약에 어떤 websocket controller가(데이터 처리를 할 필요가) 있다고 하면, /app/alram 으로 보내야 한다.
        config.enableSimpleBroker("/alram", "/message"); // 이 경로로 들어오면, message broker 가
        // 해당 경로를 가로챈다.(구독자들에게 바로 메시지를 전달)
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/reflect-socket").setAllowedOrigins(
                "ws://172.30.1.15:8080", "ws://127.0.0.1:5501", "ws://127.0.0.1:8080",
                        "http://127.0.0.1:8080", "http://127.0.0.1:5500",
                         "http://localhost:5173/", "http://127.0.0.1:5501", "http://127.0.0.1:5173",
                        "http://localhost:80", "http://127.0.0.1:80", "ws://localhost:8080",
                        "https://i10d207.p.ssafy.io:8443/api", "https://i10d207.p.ssafy.io:8443/")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration){
        //웹소켓 통신시에 jwt 검사를 한다.
        registration.interceptors(stompHandler);
    }
}
