package com.ssafy.mademe.controller;

import com.ssafy.mademe.dto.response.MyPetInfoResponse;
import com.ssafy.mademe.entity.PetCode;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.service.PetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/pet")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    //내가 가지고 있는 펫 목록 불러오기
    @GetMapping("/")
    public List<MyPetInfoResponse> getMyPetList(@AuthenticationPrincipal User user){
        return petService.getMyPetList(user);
    }

    //전체 펫 목록
    @GetMapping("/total")
    public List<PetCode> getTotalPetList(){
        return petService.getTotalPetList();
    }

    //나의 펫 정보 수정(펫 변경까지 한번에 진행)
    @PutMapping("/")
    public MyPetInfoResponse modifyPetInfo(@AuthenticationPrincipal User user, @RequestBody MyPetInfoResponse myPet){
        return petService.modifyPetInfo(user, myPet);
    }

    //펫 구매하기
    @PostMapping("/buy")
    public Map<String, String> buyPet(@AuthenticationPrincipal User user, @RequestBody MyPetInfoResponse myPet){
        petService.buyPet(user, myPet);
        Map<String, String> json = new HashMap<>();
        json.put("state", "success");
        return json;
    }
}
