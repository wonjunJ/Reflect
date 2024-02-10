package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.MyPet;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.entity.embedded.EmdPet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyPetRepository extends JpaRepository<MyPet, EmdPet> {
    MyPet findFirstById_UserAndSelectedTrue(User user);
    List<MyPet> findById_User(User user);
    MyPet findFirstById_UserAndId_PetCode_Id(User user, Long id);
    MyPet findFirstBySelectedIsTrue();
    Integer countById_User(User user);
}
