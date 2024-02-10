package com.ssafy.mademe.service;

import com.ssafy.mademe.dto.request.Message;
import com.ssafy.mademe.entity.Alram;
import com.ssafy.mademe.entity.Room;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.exception.user.UserNotFoundException;
import com.ssafy.mademe.repository.AlramRepository;
import com.ssafy.mademe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlramService {

    private final AlramRepository alramRepository;
    private final UserRepository userRepository;

    public Message manageMsg(Message msg, User sender){
        Optional<User> toId = userRepository.findById(msg.getToId());
        if(toId.isEmpty()){
            throw new UserNotFoundException("유저를 찾을 수 없습니다.");
        }
        Alram alram = new Alram();
        alram.setFrom(sender);
        alram.setUser(toId.get());
        alram.setType(msg.getType());
        if(msg.getType() == 2){ //방 초대
            Room rm = new Room();
            rm.setId(msg.getRoomId());
            alram.setInvitedRoom(rm);
        }
        alram.setContent(msg.getMsg());
        Alram al = alramRepository.save(alram);
        msg.setId(al.getId());
        msg.setSenderNick(sender.getNickName());
        return msg;
    }

    public List<Message> getUnreadAlrams(User user){
        return alramRepository.findByUserAndIsReadIsFalse(user)
                .stream()
                .map(this::mapToMessage)
                .collect(Collectors.toList());
    }

    public void readAlram(Alram alram){
        Optional<Alram> al = alramRepository.findById(alram.getId());
        if(al.isPresent()){
            al.get().setRead(true);
            alramRepository.save(al.get());
            return;
        }
        throw new UserNotFoundException("해당 알람을 찾지 못했습니다.");
    }

    private Message mapToMessage(Alram alram){
        return Message.builder()
                .id(alram.getId())
                .type(alram.getType())
                .senderId(alram.getFrom().getId())
                .senderNick(alram.getFrom().getNickName())
                .roomId(alram.getInvitedRoom() == null ? null : alram.getInvitedRoom().getId())
                .msg(alram.getContent())
                .build();
    }
}
