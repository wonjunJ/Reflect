package com.ssafy.mademe.controller;

import com.ssafy.mademe.dto.request.Message;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.exception.user.UserNotFoundException;
import com.ssafy.mademe.repository.UserRepository;
import com.ssafy.mademe.service.AlramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AlramController {

    private final SimpMessageSendingOperations messageTemplate;
    private final AlramService alramService;
    private final UserRepository userRepository;

    //웹소켓 메핑
    @MessageMapping("/alram")
    public void getMessage(Message message){
        System.out.println("웹소켓 매핑 주소로 들어왔다!!!");
        Optional<User> sender = userRepository.findById(message.getSenderId());
        if(sender.isEmpty()){
            throw new UserNotFoundException("유저를 찾을 수 없습니다.");
        }
        Message msg = alramService.manageMsg(message, sender.get());
        messageTemplate.convertAndSend("/alram/msg-to/" + message.getToId(), msg);
    }
}
