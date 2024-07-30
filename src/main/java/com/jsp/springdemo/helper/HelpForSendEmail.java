package com.jsp.springdemo.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.jsp.springdemo.dto.MyUser;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;

@Component
public class HelpForSendEmail {
    @Autowired
    JavaMailSender mailSender;

    public void Sendmail(@Valid MyUser myUser) {
        MimeMessage mimeMessage=mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(mimeMessage);
        try {
            helper.setFrom("vineeeth2429@gmail.com");
            helper.setTo(myUser.getEmail());
            helper.setSubject("otp for varefy");
            helper.setText("hello"+myUser.getName()+"your otp is : "+myUser.getOtp());
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mailSender.send(mimeMessage);
    }


}
