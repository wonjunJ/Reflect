package com.ssafy.mademe.service;

import com.ssafy.mademe.dto.response.MyPetInfoResponse;
import com.ssafy.mademe.entity.MyPet;
import com.ssafy.mademe.entity.PetCode;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.entity.embedded.EmdPet;
import com.ssafy.mademe.exception.pet.DuplicatePetException;
import com.ssafy.mademe.exception.pet.PetNotFoundException;
import com.ssafy.mademe.exception.user.NotEnoughPointException;
import com.ssafy.mademe.exception.user.UserNotFoundException;
import com.ssafy.mademe.repository.MyPetRepository;
import com.ssafy.mademe.repository.PetCodeRepository;
import com.ssafy.mademe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetService {

    private final MyPetRepository myPetRepository;
    private final PetCodeRepository petCodeRepository;
    private final UserRepository userRepository;

    public List<MyPetInfoResponse> getMyPetList(User user){
        List<MyPet> petList = myPetRepository.findById_User(user);
        return petList
                .stream()
                .map(this::mapToMyPetInfoResponse)
                .collect(Collectors.toList());
    }

    public List<String> getUserPetInfo(User user){
        MyPet petInfo = myPetRepository.findFirstById_UserAndSelectedTrue(user);
        List<String> infoLs = new ArrayList<>();
        if(petInfo != null){
            infoLs.add(petInfo.getId().getPetCode().getPetName()); //펫의 원래 이름
            infoLs.add(petInfo.getMypetName()); //내가 설정한 펫의 이름
        }
        return infoLs;
    }

    public MyPetInfoResponse modifyPetInfo(User user, MyPetInfoResponse myPet){
        MyPet pet = myPetRepository.findFirstById_UserAndId_PetCode_Id(user, myPet.getPetId()); //내가 새로 선택한 펫
        if(pet == null){
            throw new PetNotFoundException("내가 가지고 있는 펫이 아닙니다.");
        }
        MyPet before = myPetRepository.findFirstBySelectedIsTrue(); //내가 이전에 선택(사용)했던 펫
        before.setSelected(false);
        myPetRepository.save(before);

        pet.setMypetName(myPet.getMypetName());
        pet.setSelected(true);
        return mapToMyPetInfoResponse(myPetRepository.save(pet));
    }

    @Transactional
    public void buyPet(User user, MyPetInfoResponse pet){
        Optional<PetCode> code = petCodeRepository.findById(pet.getPetId());
        if(code.isPresent()){
            EmdPet id = new EmdPet(user, code.get());
            if(myPetRepository.existsById(id)){
                throw new DuplicatePetException("이미 나에게 존재하는 펫입니다.");
            }
            MyPet myPet = new MyPet();
            myPet.setId(id);
            myPet.setMypetName(code.get().getPetName()); //나의 펫 이름 초기값은 펫의 원래 이름
            if(user.getPoint() < pet.getPrice()){
                throw new NotEnoughPointException("포인트가 부족합니다.");
            }
            myPetRepository.save(myPet);
            int curPoint = user.getPoint();
            user.setPoint(curPoint-pet.getPrice());
            userRepository.save(user);
        }
        throw new PetNotFoundException("일치하는 펫을 찾을 수 없습니다.");
    }

    public MyPetInfoResponse mapToMyPetInfoResponse(MyPet pet){
        return MyPetInfoResponse.builder()
                .petId(pet.getId().getPetCode().getId())
                .petName(pet.getId().getPetCode().getPetName())
                .mypetName(pet.getMypetName())
                .build();
    }
}
