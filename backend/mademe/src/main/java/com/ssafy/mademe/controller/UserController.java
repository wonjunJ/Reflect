package com.ssafy.mademe.controller;

import com.ssafy.mademe.dto.request.ChangePwRequest;
import com.ssafy.mademe.dto.request.CheckCodeRequest;
import com.ssafy.mademe.dto.request.KakaoUserInfoRequest;
import com.ssafy.mademe.dto.request.Message;
import com.ssafy.mademe.dto.response.*;
import com.ssafy.mademe.entity.Alram;
import com.ssafy.mademe.entity.Interest;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.exception.user.DuplicateUserIdException;
import com.ssafy.mademe.exception.user.InvalidPasswordException;
import com.ssafy.mademe.service.*;
import com.ssafy.mademe.utils.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MailService mailService;
    private final EmotionService emotionService;
    private final InterestService interestService;
    private final PetService petService;
    private final JWTUtil jwtUtil;
    private final AlramService alramService;
    private final SimpMessageSendingOperations messageTemplate;

    private int cookieAge = 60 * 60 * 24 * 365 * 40;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User loginRequest, HttpServletResponse response){
//        @Value("${spring.datasource.url}")
//        String tmp;
        Map<String, String> tokens = userService.doLogin(loginRequest.getSiteId(), loginRequest.getPw());
        setCookie(loginRequest, response);
        //tokens.put("id", tmp);
        return tokens;
    }

    @PostMapping("/join")
    public Map<String, String> join(@RequestBody User user){
        userService.joinMember(user);
        Map<String, String> state = new HashMap<>();
        state.put("state", "success");
        return state;
    }

    //소셜 회원가입과 로그인을 동시에 진행
    @PostMapping("/social/join")
    public Map<String, String> oauthJoinAndLogin(@RequestBody KakaoUserInfoRequest user){
        return userService.oauthJoin(user);
    }

    //출석체크(당일 기준으로 할지, 24시간 기준으로 할지)-일단 당일 기준
    @PostMapping("/attendance")
    public Map<String, String> attendanceCheck(@AuthenticationPrincipal User user){
        userService.attendanceCheck(user);
        Map<String, String> json = new HashMap<>();
        json.put("state", "출석체크를 완료하였습니다.");
        return json;
    }

    //포인트 획득(포춘쿠키에서)
    @PutMapping("/point")
    public Map<String, String> getPoint(@AuthenticationPrincipal User user, @RequestParam("point") int point){
        userService.getPoint(user, point);
        Map<String, String> json = new HashMap<>();
        json.put("state", "포인트를 획득하였습니다.");
        return json;
    }

    //회원정보 조회
    @GetMapping("/")
    public UserInfoResponse getUserInfo(@AuthenticationPrincipal User user){ //쿼리문을 3번???
        String mbtiStr = userService.getUserMbtiInfo(user);
        List<String> titleList = userService.getTitles(user); //정보가 없다면 빈 리스트가 리턴
        List<String> petInfo = petService.getUserPetInfo(user);
        List<Message> msgList = alramService.getUnreadAlrams(user);
        UserInfoResponse uires = mapToUserInfo(user, mbtiStr, titleList);
        if(!petInfo.isEmpty()){
            uires.setCharId(Long.parseLong(petInfo.get(0)));
            uires.setCharName(petInfo.get(1)); //펫의 원래 이름
            uires.setMyCharName(petInfo.get(2)); //내가 설정한 펫의 이름
        }
        uires.setAlrams(msgList);
        //System.out.println(LocalDateTime.now());
        return uires;
    }

    //알람 읽음 처리하기
    @PutMapping("/alram")
    public Map<String, String> readAlram(@RequestBody Alram alram){
        alramService.readAlram(alram);
        Map<String, String> json = new HashMap<>();
        json.put("state", "알람 읽음 처리 완료");
        return json;
    }

    //회원정보 수정
    @PutMapping("/")
    public UserInfoResponse modifyUserInfo(@RequestBody UserInfoResponse user, @AuthenticationPrincipal User token){
        return mapToUserInfo(userService.modifyUserInfo(user, token), "", new ArrayList<>());
    }

    //마이페이지에서 간단한 감정과 관심사 정보 조회
    @GetMapping("/simple")
    public SimpleLogInfoResponse getSimpleLogInfo(@AuthenticationPrincipal User user){
        EmotionCntResponse emo = emotionService.getEmoCnt(user, 7);
        List<InterestResponse> inter = interestService.getTop5Interest(user);
        return new SimpleLogInfoResponse(emo, inter);
    }

    @PostMapping("/send")
    public String sendMail(@RequestBody User user) throws Exception {
        userService.checkUser(user);
        mailService.sendCertificationMail(user);
        return "인증번호 전송 완료";
    }

    //아이디 중복 체크
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

    //닉네임 중복 체크
    @GetMapping("/nick/{nickname}")
    public void nickCheck(@PathVariable("nickname") String nickName) {
        boolean rst = userService.nickCheck(nickName);
        if(rst){
            throw new DuplicateUserIdException("중복된 유저 닉네임입니다.");
            //throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "중복된 닉네임입니다.");
        }
    }

    //아이디 찾기
    @GetMapping("/findid")
    public Map<String, String> findId(@RequestParam("email") String email){
        return userService.findId(email);
    }

    //인증번호 확인
    @PostMapping("/code")
    public CheckCodeResponse checkCode(@RequestBody CheckCodeRequest checkCodeRequest){
        return userService.checkCode(checkCodeRequest);
    }

    //비밀번호 찾기
    @PostMapping("/findpw")
    public String findPw(@RequestBody CheckCodeRequest checkCodeRequest){
        CheckCodeResponse rs = userService.checkCode(checkCodeRequest);
        userService.changePw(rs.getEmail(), checkCodeRequest.getPw());
        return "비밀번호 변경 완료";
    }

    //비밀번호 변경(내 정보 수정에서)
    @PutMapping("/changepw")
    public String changePw(@RequestBody ChangePwRequest pwInfo, @AuthenticationPrincipal User user){
        boolean rst = userService.checkCurPw(user.getId(), pwInfo.getCurrent_pw());
        if(rst){
            userService.changePw(user, pwInfo.getNew_pw());
            return "비밀번호 변경 완료";
        }
        throw new InvalidPasswordException("현재 비밀번호가 올바르지 않습니다.");
    }

    @PostMapping("/logout")
    public Map<String, String> logout(@AuthenticationPrincipal User user) {
        Long userId = user.getId();
        log.info(userId.toString());
        jwtUtil.doLogout(userId);
        Map<String, String> state = new HashMap<>();
        state.put("state", "success");
        return state;
    }

    @DeleteMapping("/")
    public String doWithdrawal(@AuthenticationPrincipal User user){
        userService.doWithdrawal(user);
        return "회원탈퇴 완료";
    }

    //refresh token 재발급
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

    private UserInfoResponse mapToUserInfo(User user, String ls, List<String> titles){
        return UserInfoResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .nickName(user.getNickName())
                .email(user.getEmail())
                .profileImg(user.getProfileImg())
                .gender(user.getGender())
                .phoneNumber(user.getPhoneNumber())
                .birthday(user.getBirthday())
                .mbti(ls)
                .point(user.getPoint())
                .titles(titles)
                .myIntro(user.getMyIntro())
                .isOauth(user.isOauth())
                .build();
    }
}
