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
        message.setSubject("Enjoy Trip 인증 번호입니다.");
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
            throw   new IllegalAccessException();
        }
    }

    public void sendCertificationMail(User user) throws Exception {
        StringBuilder email = new StringBuilder();
//        email.append(user.getEmail_id());
//        email.append("@");
//        email.append(user.getEmail_domain());
        String  code = UUID.randomUUID().toString().substring(0, 6);
        Instant start = new Date().toInstant();
        Instant end = new Date(System.currentTimeMillis() + expireTimeMs).toInstant();
        redisTemplate.opsForValue().set("ecode:" + email.toString(), code, Duration.between(start, end));
        sendMail(code, email.toString());
    }
}
