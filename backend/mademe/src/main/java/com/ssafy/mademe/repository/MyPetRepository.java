package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.MyPet;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.entity.embedded.EmdPet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyPetRepository extends JpaRepository<MyPet, EmdPet> {
    MyPet findFirstById_UserAndSelectedTrue(User user);
}
