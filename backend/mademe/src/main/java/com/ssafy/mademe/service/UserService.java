package com.ssafy.mademe.service;

import com.ssafy.mademe.dto.UserMBTIMethod;
import com.ssafy.mademe.dto.request.CheckCodeRequest;
import com.ssafy.mademe.dto.request.KakaoUserInfoRequest;
import com.ssafy.mademe.dto.response.CheckCodeResponse;
import com.ssafy.mademe.dto.response.MyPetInfoResponse;
import com.ssafy.mademe.dto.response.UserInfoResponse;
import com.ssafy.mademe.entity.*;
import com.ssafy.mademe.entity.embedded.EmdPet;
import com.ssafy.mademe.exception.user.*;
import com.ssafy.mademe.repository.*;
import com.ssafy.mademe.utils.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final MBTILogRepository mbtiLogRepository;
    private final UserTitleRepository userTitleRepository;
    private final MyPetRepository myPetRepository;
    private final PetCodeRepository petCodeRepository;
    private final BCryptPasswordEncoder encoder;
    private final RedisTemplate<Object, Object> redisTemplate;
    private final JWTUtil jwtUtil;
    private final MailService mailService;
    private final UserMBTIMethod userMBTIMethod = new UserMBTIMethod();

    public Map<String, String> doLogin(String userId, String userPw){
        User user = userRepository.findBySiteId(userId);
        if(user == null || user.isWithdrawal()){
            //throw new ResponseStatusException(HttpStatus.NOT_FOUND, "아이디 또는 비밀번호 확인 후 다시 로그인하세요!");
            throw new UserNotFoundException("아이디 또는 비밀번호 확인 후 다시 로그인하세요!");
        }
        if(!encoder.matches(userPw, user.getPw())){
            //throw new ResponseStatusException(HttpStatus.NOT_FOUND, "비밀번호가 올바르지 않습니다.");
            throw new InvalidPasswordException("비밀번호가 올바르지 않습니다.");
        }
        return tokensToMap(user.getId());
    }

    @Transactional
    public void joinMember(User user) {
        try {
            String password = user.getPw();
            user.setPw(encoder.encode(password));
            User joined = userRepository.save(user);
            provideBasicPet(joined);
        }catch (Exception e){
            throw new FailedJoinException("회원가입에 실패하였습니다.");
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "회원가입에 실패하였습니다.");
        }
    }

    @Transactional
    public Map<String, String> oauthJoin(KakaoUserInfoRequest user){
        try {
            User joinUser = new User();
            joinUser.setOauth(true);
            joinUser.setSiteId(user.getId());
            joinUser.setName(user.getName());
            joinUser.setNickName(user.getNickName()); //사용자에게 추가로 입력받는 것
            joinUser.setEmail(user.getEmail()); //사용자에게 추가로 입력받는 것
            joinUser.setProfileImg(user.getProfileImg());
            User fuser = userRepository.save(joinUser);
            provideBasicPet(fuser);
            return tokensToMap(fuser.getId());
        }catch (Exception e){
            throw new FailedJoinException("회원가입에 실패하였습니다.");
        }
    }

    //가입시 기본 펫 제공
    public void provideBasicPet(User user){
        Optional<PetCode> code = petCodeRepository.findById(1L);
        EmdPet id = new EmdPet(user, code.get()); //PetCode테이블에 기본값 집어넣기
        MyPet myPet = new MyPet();
        myPet.setId(id);
        myPet.setMypetName(code.get().getPetName()); //나의 펫 이름 초기값은 펫의 원래 이름
        myPet.setSelected(true);
        myPetRepository.save(myPet);
    }

    public void attendanceCheck(User user){
        LocalDate now = LocalDate.now();
        if(user.getCheckDate() == null || user.getCheckDate().toLocalDate().isBefore(now)){
            int curPoint = user.getPoint();
            user.setPoint(curPoint + 5);
            user.setCheckDate(LocalDateTime.now());
            userRepository.save(user);
            return;
        }
        throw new AlreadyAttendanceException("이미 오늘 출석체크를 하였습니다.");
    }

    public void getPoint(User user, int point){ //point는 획득할 포인트
        int curPoint = user.getPoint();
        user.setPoint(curPoint + point);
        userRepository.save(user);
    }

    public String getUserMbtiInfo(User user){
        //유저의 mbti정보만 얻는다.
        Map<MBTICode, Long> map = mbtiLogRepository.findMbtiByUser(user);
        return userMBTIMethod.getMBTIs(map);
    }

    public List<String> getTitles(User user){
        List<UserTitle> titleList = userTitleRepository.findById_User(user);
        List<String> ls = new ArrayList<>();
        for(UserTitle ut : titleList){
            ls.add(ut.getId().getTCode().getTitleName());
        }
        return ls;
    }

    public User modifyUserInfo(UserInfoResponse modified, User origin){ //값이 변경되지 않았을 경우에도 원래의 값을 받아야 한다.
//        if(modified.getEmail() != null && userRepository.existsByEmail(modified.getEmail())){
//            throw new DuplicateUserIdException("중복된 이메일입니다.");
//        }
        //이메일 수정은 없다.
        origin.setName(modified.getName());
        origin.setNickName(modified.getNickName());
        origin.setEmail(modified.getEmail());
        origin.setProfileImg(modified.getProfileImg());
        origin.setPhoneNumber(modified.getPhoneNumber());
        origin.setGender(modified.getGender());
        origin.setBirthday(modified.getBirthday());
        origin.setMyIntro(modified.getMyIntro());
        return userRepository.save(origin);
    }

    public void checkUser(User user) {
        if(user.getSiteId() == null && userRepository.existsByEmail(user.getEmail())){ //회원가입을 할때 보내는 인증번호(이메일 중복 검사를 한번 거친다)
            throw new DuplicateUserIdException("이미 가입된 이메일입니다.");
            //throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유저 정보가 일치하지 않습니다.");
        } else if (user.getSiteId() != null) { //비밀번호 찾기 할때 보내는 인증번호
            if(!userRepository.existsBySiteId(user.getSiteId())){
                throw new UserNotFoundException("가입된 아이디가 아닙니다.");
            }
        }
    }

    public boolean idCheck(String userId) {
        return userRepository.existsBySiteId(userId);
    }

    public boolean nickCheck(String nickName) {
        return userRepository.existsByNickName(nickName);
    }

    public Map<String, String> findId(String email){
        Map<String, String> json = new HashMap<>();
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new UserNotFoundException("아이디를 찾을 수 없습니다.");
        }
        json.put("user_id", user.getSiteId());
        return json;
    }

    public void changePw(Object obj, String pw){
        try {
            User user;
            if (obj instanceof User){ //비밀번호 변경 메소드 넘어왔다.
                user = (User)obj;
                System.out.println(user.getId());
            }else { //비밀번호 찾기 메소드에서 넘어왔다. 이메일로 유저정보를 뽑아낸다.
                user = userRepository.findByEmail((String) obj);
            }
            user.setPw(encoder.encode(pw));
            //user객체에 Id값이 들어가 있기 때문에 update쿼리
            userRepository.save(user);
        }catch (Exception e){
            throw new FailedJoinException("비밀번호 변경에 실패했습니다.");
        }
    }

    public boolean checkCurPw(Long userId, String pw){
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            return encoder.matches(pw, user.get().getPassword());
            //return user.get().getPw().equals(pw);
        }else{
            throw new UserNotFoundException("일치하는 회원정보가 없습니다.");
        }
    }

    public CheckCodeResponse checkCode(@RequestBody CheckCodeRequest request){
        String email = request.getEmail();
        String code = (String) redisTemplate.opsForValue().get("ecode:" + email);
        if(code != null){
            if(code.equals(request.getCode())){ //보낸 인증번호와 유저가 입력한 인증번호가 일치
                System.out.println("인증번호 일치");
                //redisTemplate.delete("ecode:" + email);
                String tcode = mailService.createRandNum();
                redisTemplate.opsForValue().set("ecode:" + email, tcode, Duration.ofMinutes(3));
                return CheckCodeResponse.builder()
                        .email(email)
                        .code(tcode)
                        .msg("인증번호 확인 완료")
                        .build();
            }else{
                //417 상태코드
                throw new InvalidCodeException("인증번호가 일치하지 않습니다");
                //throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "인증번호가 일치하지 않습니다");
            }
        }else{
            //406 상태코드
            throw new TimeOutCodeException("요청된 인증번호가 없습니다.");
            //throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "요청된 인증번호가 없습니다.");
        }
    }

    public void doWithdrawal(User user){
        user.setWithdrawal(true);
        //user객체에 Id값이 들어가 있기 때문에 update쿼리
        userRepository.save(user);
    }

    public Map<String, String> reissuedToken(HttpServletRequest request){
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];
        Long userid = jwtUtil.getUserId(token);
        //정상적인 refresh 토큰이다
        if(token.equals(redisTemplate.opsForValue().get("RT:" + userid))){
            String[] tokens = jwtUtil.createAndGetTokens(userid);
            Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put("AccessToken", tokens[0]);
            tokenMap.put("RefreshToken", tokens[1]);
            return tokenMap;
        }
        throw new InvalidTokenException("올바른 refresh token이 아닙니다.");
        //throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "올바른 refresh token이 아닙니다.");
    }

    public Map<String, String> tokensToMap(Long id){
        String[] tokens = jwtUtil.createAndGetTokens(id);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("userId", id.toString()); //STOMP 웹소켓 통신을 위한 구독을 하기 위해
        tokenMap.put("AccessToken", tokens[0]);
        tokenMap.put("RefreshToken", tokens[1]);
        return tokenMap;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        System.out.println(userId);
        Optional<User> foundUser = userRepository.findById(Long.valueOf(userId));
        System.out.println(foundUser.get().getName());

        if(foundUser.isPresent()){
            return foundUser.get();
        }
        throw new UserNotFoundException("아이디 또는 비밀번호를 찾을 수 없습니다.");
        //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호를 찾을 수 없습니다.");
    }
}
