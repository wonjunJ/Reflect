package com.ssafy.mademe.controller;

import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.service.MailService;
import com.ssafy.mademe.service.UserService;
import com.ssafy.mademe.utils.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MailService mailService;
    private final JWTUtil jwtUtil;

    private int cookieAge = 60 * 60 * 24 * 365 * 40;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User loginRequest, HttpServletResponse response){
        Map<String, String> tokens = userService.doLogin(loginRequest.getSiteId(), loginRequest.getPw());
        setCookie(loginRequest, response);
        return tokens;
    }

    @PostMapping("/join")
    public String join(@RequestBody User user){
        userService.joinMember(user);
        return "success";
    }

    @GetMapping("/id/{userid}")
    public void idCheck(@PathVariable("userid") String userId) {
        log.info("idCheck 진입");
        boolean rst = userService.idCheck(userId);
        if(rst){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "중복된 유저 아이디입니다.");
        }
    }

    @GetMapping("/nick/{nickname}")
    public void nickCheck(@PathVariable("nickname") String nickName) {
        boolean rst = userService.nickCheck(nickName);
        if(rst){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "중복된 닉네임입니다.");
        }
    }

    @PostMapping("/logout")
    public void logout(@AuthenticationPrincipal User user) {
        String userId = user.getSiteId();
        jwtUtil.doLogout(userId);
    }

//    @PostMapping("/reissue")
//    public Map<String, String> reissuedToken(){
//        Map<String, String> tokens =
//    }


    private void setCookie(User loginRequest, HttpServletResponse response){
        Cookie cookie = new Cookie("ssafy_id", loginRequest.getSiteId());
        cookie.setPath("/user");

        if("ok".equals(loginRequest.getSaveId())) {
            cookie.setMaxAge(cookieAge);
        } else {
            cookie.setMaxAge(0);
        }

        response.addCookie(cookie);
    }
}
