package com.ssafy.mademe.repository;

import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.entity.UserTitle;
import com.ssafy.mademe.entity.embedded.EmdTitle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTitleRepository extends JpaRepository<UserTitle, EmdTitle> {
    List<UserTitle> findById_User(User user);
}
