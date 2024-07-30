package com.jsp.springdemo.reposetry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.jsp.springdemo.dto.StudentData;
@Component
public interface MyStudentReposetry extends JpaRepository<StudentData,Integer> {

    StudentData findByName(String name);

    void deleteByName(String name);

    
   
}
