package com.ssafy.mademe.service;

import com.ssafy.mademe.dto.request.Message;
import com.ssafy.mademe.entity.Alram;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.repository.AlramRepository;
import com.ssafy.mademe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlramService {

    private final AlramRepository alramRepository;
    private final UserRepository userRepository;

    public void manageMsg(Message msg){
        Optional<User> sender = userRepository.findById(msg.getSender());
        Optional<User> toId = userRepository.findById(msg.getToId());
        Alram alram = new Alram();
        alram.setFrom(sender.get());
        alram.setUser(toId.get());
        alram.setType(msg.getType());
        if(msg.getType() == 1){
            //alram.setInvitedRoom(msg.getRoomId());
        }
        alram.setContent(msg.getMsg());
        alramRepository.save(alram);
    }
}
