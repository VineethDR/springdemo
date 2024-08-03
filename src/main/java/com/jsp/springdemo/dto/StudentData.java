package com.jsp.springdemo.dto;



import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
@Entity
@Data
public class StudentData {
    @Id
    @GeneratedValue(generator = "x")
    @SequenceGenerator(name = "x",initialValue = 101,allocationSize = 1)
private int id;
private String name;
private String stdclass;
private String mobile;
private LocalDate dob;
private int subject1marks;
private int subject2marks;
private int subject3marks;
private int subject4marks;
private int subject5marks;
private int subject6marks;
private String picher;
}
