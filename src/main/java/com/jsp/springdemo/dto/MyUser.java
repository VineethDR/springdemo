package com.jsp.springdemo.dto;

import com.jsp.springdemo.helper.AES;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class MyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;
private int otp;
private boolean verified;
@Email(message = "*Enter the proper email")
@NotEmpty(message = "*this is required")
private String email;
@Size(min = 3,max = 20,message = "*Enter the proper name")
private String name;
@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$", message = "* Enter more than 8 characters Consisting of One Upper Case, One Lower Case, One Special Charecter, One Number")
private String password;
public void setPassword(String password){
    this.password=AES.encrypt(password, "489726");
}
public String getPassword(){
    return AES.decrypt(this.password, "489726");
}
}
