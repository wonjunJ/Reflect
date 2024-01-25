package com.ssafy.mademe.controller;

import com.ssafy.mademe.dto.request.ChangePwRequest;
import com.ssafy.mademe.dto.request.CheckCodeRequest;
import com.ssafy.mademe.dto.response.CheckCodeResponse;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.exception.user.DuplicateUserIdException;
import com.ssafy.mademe.service.MailService;
import com.ssafy.mademe.service.UserService;
import com.ssafy.mademe.utils.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/send")
    public String sendMail(@RequestBody User user) throws Exception {
        userService.checkUser(user);
        mailService.sendCertificationMail(user);
        return "인증번호 전송 완료";
    }

    @GetMapping("/id/{userid}")
    public void idCheck(@PathVariable("userid") String userId) {
        log.info("idCheck 진입");
        System.out.println("다시한번 체크");
        boolean rst = userService.idCheck(userId);
        log.info(String.valueOf(rst));
        if(rst){
            log.info("여기로 들어왔다.");
            throw new DuplicateUserIdException("중복된 유저 아이디입니다.");
            //throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "중복된 유저 아이디입니다.");
        }
    }

    @GetMapping("/nick/{nickname}")
    public void nickCheck(@PathVariable("nickname") String nickName) {
        boolean rst = userService.nickCheck(nickName);
        if(rst){
            throw new DuplicateUserIdException("중복된 유저 닉네임입니다.");
            //throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "중복된 닉네임입니다.");
        }
    }

    @GetMapping("/findid")
    public Map<String, String> findId(@RequestBody User user){
        return userService.findId(user.getEmail());
    }

    @PostMapping("/code")
    public CheckCodeResponse checkCode(@RequestBody CheckCodeRequest checkCodeRequest){
        return userService.checkCode(checkCodeRequest);
    }

    @PostMapping("/findpw")
    public String findPw(@RequestBody CheckCodeRequest checkCodeRequest){
        userService.checkCode(checkCodeRequest);
        userService.changePw(checkCodeRequest.getPw());
        return "비밀번호 변경 완료";
    }

    @PutMapping("/changepw")
    public String changePw(@RequestBody ChangePwRequest pwInfo, @AuthenticationPrincipal User user){
        boolean rst = userService.checkCurPw(user.getId(), pwInfo.getCurrent_pw());
        if(rst){
            return "완료";
        }
        return "실패";
    }

    @PostMapping("/logout")
    public void logout(@AuthenticationPrincipal User user) {
        Long userId = user.getId();
        log.info(userId.toString());
        jwtUtil.doLogout(userId);
    }

    @PostMapping("/refresh")
    public Map<String, String> reissuedToken(HttpServletRequest request){
        return userService.reissuedToken(request);
    }


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
