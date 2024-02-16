package com.ssafy.mademe.config.security;

import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.repository.UserRepository;
import com.ssafy.mademe.service.OAuth2UserService;
import com.ssafy.mademe.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final OAuth2UserService oAuth2UserService;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final OAuth2SuccessHandler successHandler;
    private final SimpMessageSendingOperations messageTemplate;
    private RedirectStrategy redirectStrategy;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/user/join", "/user/login", "/user/refresh",
                                "/user/id/*", "/user/nick/*", "/user/social/*", "/user/send", "/user/findid",
                                "/user/code", "/user/findpw", "/reflect-socket/**", "/sessions", "/sessions/**").permitAll()
                        .requestMatchers("/user/*","/DiaryDetail/*", "/follow/*", "/app/*", "/diary/*",
                                "/interest/*", "/question/*", "/room/*", "/emotion/*",
                                "/pet/*", "/sessions", "/sessions/**").hasRole("USER")
                        .anyRequest().authenticated())
                .oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer
                        .loginPage("/login")
                        .successHandler(successHandler)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService)
                        )
                )
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .anonymous(Customizer.withDefaults())
                .exceptionHandling((handling) ->
                        handling.accessDeniedHandler(jwtAccessDeniedHandler)
                                .authenticationEntryPoint(authenticationEntryPoint())
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:8080",
                "http://127.0.0.1:5500", "http://localhost:5173", "http://127.0.0.1:5173",
                "https://i10d207.p.ssafy.io:8443/" , "https://i10d207.p.ssafy.io:8443/api"
        ));

        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() { //카카오 OAuth 인증 성공시 실행
        return ((request, response, authentication) -> {
            DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
            String frontUri = request.getParameter("redirect_uri");
            System.out.println(frontUri);

            //System.out.println(defaultOAuth2User.getAttributes());

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
            redirectStrategy.sendRedirect(request, response, redirectUrl);

//            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
//
//            PrintWriter writer = response.getWriter();
//            writer.println(body);
//            //Front로 데이터를 전송
//            writer.flush();
        });
    }
}
