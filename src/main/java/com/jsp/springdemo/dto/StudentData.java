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
private double subject1marks;
private double subject2marks;
private double subject3marks;
private double subject4marks;
private double subject5marks;
private double subject6marks;
private String picher;
}
