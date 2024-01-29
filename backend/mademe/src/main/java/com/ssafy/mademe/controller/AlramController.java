package com.ssafy.mademe.controller;

import com.ssafy.mademe.dto.request.Message;
import com.ssafy.mademe.service.AlramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AlramController {

    private final SimpMessageSendingOperations messageTemplate;
    private final AlramService alramService;

    //웹소켓 메핑
    @MessageMapping("/")
    public void getMessage(Message message){
        alramService.manageMsg(message);
        messageTemplate.convertAndSend("/alarm/msg-to/" + message.getToId(), message);
    }
}
