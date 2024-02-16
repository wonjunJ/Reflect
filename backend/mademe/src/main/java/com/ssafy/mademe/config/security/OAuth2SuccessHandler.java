package com.ssafy.mademe.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.repository.UserRepository;
import com.ssafy.mademe.utils.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        //String frontUri = request.getParameter("redirect_uri");
        String frontUri = "https://i10d207.p.ssafy.io:8443/social"; //프론트 주소에 맞게 바꾸기
        System.out.println(frontUri);

        String id = defaultOAuth2User.getAttributes().get("id").toString();
        String properties = defaultOAuth2User.getAttributes().get("properties").toString();
        String[] parts = properties.split(",");

        // 1. 닉네임 추출
        String nickname = parts[0].split("=")[1];
        System.out.println("닉네임: " + nickname);

        // 2. 프로필 이미지 URL 추출
        String profileImage = parts[1].split("=")[1];
        System.out.println("프로필 이미지 URL: " + profileImage);

        // 3. 썸네일 이미지 URL 추출
        String thumbnailImage = parts[2].split("=")[1].replace("}", "");
        System.out.println("썸네일 이미지 URL: " + thumbnailImage);

        Boolean isLogin = userRepository.existsBySiteIdAndIsOauthIsTrue(id);
        StringBuilder body = new StringBuilder();
        body.append(
                String.format("{\"id\":\"%s\", \"nickname\":\"%s\", \"profileImage\":\"%s\", \"thumbnailImage\":\"%s\", \"isLogin\":\"%s\"",
                        id, nickname, profileImage, thumbnailImage, isLogin.toString())
        );
        if(isLogin){
            User user = userRepository.findBySiteId(id);
            String[] tokens = jwtUtil.createAndGetTokens(user.getId());
            body.append(String.format(", \"userId\":\"%d\", \"AccessToken\":\"%s\", \"RefreshToken\":\"%s\"",
                    user.getId(), tokens[0], tokens[1]));
        }
        body.append("}");

        //messageTemplate.convertAndSend("/alarm/msg-to/" + message.getToId(), message);

        String redirectUrl = frontUri + "?data=" + URLEncoder.encode(body.toString(), StandardCharsets.UTF_8);
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
