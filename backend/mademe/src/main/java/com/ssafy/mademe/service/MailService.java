package com.ssafy.mademe.service;

import com.ssafy.mademe.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailService {

    private final RedisTemplate<Object, Object> redisTemplate;
    private final JavaMailSender javaMailSender;

    private static final Long expireTimeMs = 1000 * 60L; //1분 설정

    private MimeMessage createMessage(String code, String email) throws Exception{
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, email); //수신자 지정하기
        message.setSubject("Reflect 인증 번호입니다.");
        message.setText("이메일 인증코드: "+code);

        message.setFrom(new InternetAddress("museoul@naver.com"));

        return  message;
    }

    public void sendMail(String code, String email) throws Exception{
        try{
            MimeMessage mimeMessage = createMessage(code, email);
            javaMailSender.send(mimeMessage);
        }catch (MailException mailException){
            mailException.printStackTrace();
            throw new IllegalAccessException();
        }
    }

    public void sendCertificationMail(User user) throws Exception {
        String email = user.getEmail();
        String  code = createRandNum();
        //String  code = UUID.randomUUID().toString().substring(0, 6);
        redisTemplate.opsForValue().set("ecode:" + email, code, Duration.ofMinutes(2));
        sendMail(code, email);
    }

    public String createRandNum(){
        Random random = new Random();		//랜덤 함수 선언
        int createNum = 0;  			//1자리 난수
        int letter    = 6;			//난수 자릿수:6
        StringBuilder resultNum = new StringBuilder();  		//결과 난수

        for (int i=0; i<letter; i++) {
            createNum = random.nextInt(9);		//0부터 9까지 올 수 있는 1자리 난수 생성
//            ranNum =  Integer.toString(createNum);  //1자리 난수를 String으로 형변환
            resultNum.append(createNum);			//생성된 난수(문자열)을 원하는 수(letter)만큼 더하며 나열
        }
        return resultNum.toString();
    }
}
