package com.jsp.springdemo.reposetry;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.springdemo.dto.MyUser;

public interface MyUserReposetry extends JpaRepository<MyUser,Integer>{

    boolean existsByEmail(String email);

    MyUser findByEmail(String email);

}
