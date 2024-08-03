package com.jsp.springdemo.controler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.jsp.springdemo.dto.MyUser;
import com.jsp.springdemo.dto.StudentData;
import com.jsp.springdemo.helper.HelpForSendEmail;
import com.jsp.springdemo.reposetry.MyStudentReposetry;
import com.jsp.springdemo.reposetry.MyUserReposetry;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestBody;





@Controller
@MultipartConfig
public class mycontroler {

    @Autowired
    MyUserReposetry myUserReposetry;

    @Autowired
    MyStudentReposetry myStudentReposetry;

    @Autowired
    HelpForSendEmail helpForSendEmail;

    @GetMapping("/")
    public String lodeHome() {
        return "home.html";
    }
    @GetMapping("/signup")
    public String lodesinin(ModelMap map) {
        map.put("myUser",new MyUser());
        return "signup.html";
    }
    @GetMapping("/login")
    public String lodinglogin() {
        return "login.html";
    }
    
    
    @PostMapping("/signup")
    public String signup(@Valid MyUser myUser,BindingResult result,ModelMap map) {
        if(myUserReposetry.existsByEmail(myUser.getEmail()))
        result.rejectValue("email", "error.email", "email alredy exist");;
       if (result.hasErrors()) {
        return "signup.html";
       }else{
        int otp=new Random().nextInt(100000,1000000);
        myUser.setOtp(otp);
        helpForSendEmail.Sendmail(myUser);
        myUserReposetry.save(myUser);
        map.put("success", "otp sent success, to your emailid");
        map.put("id", myUser.getId());
        return "enter_otp.html";
       }
    }
    @PostMapping("/varify-otp")
    public String verify(@RequestParam int id,@RequestParam int otp,ModelMap map) {
       MyUser myUser=myUserReposetry.findById(id).orElseThrow();
       if (myUser.getOtp()==otp) {
        myUser.setVerified(true);
        myUserReposetry.save(myUser);
        map.put("success", "verified successfully");
        return "home.html";
       }else{
        map.put("falure", "try agin");
            map.put("id",myUser.getId());
            return "enter_otp.html";
       }
    }
    @PostMapping("/login")
    public String login(@RequestParam String email,@RequestParam String password,ModelMap map,HttpSession session) {
        MyUser myUser=myUserReposetry.findByEmail(email);
        if (myUser==null) {
            map.put("nullemail", "*enter the valid email");
            return "login.html";
        }else{
        if (myUser.getPassword().equals(password)) {
            if (myUser.isVerified()) {
                session.setAttribute("user", myUser);
                map.put("successmain", "login successfully");
                return "home.html";
            }else{
                map.put("falureverifi", "*your acount is not verified");
                int otp=new Random().nextInt(100000,1000000);
                myUser.setOtp(otp);
                helpForSendEmail.Sendmail(myUser);
                myUserReposetry.save(myUser);
                map.put("success", "otp sent success, to your emailid");
                map.put("id",myUser.getId());
                return "enter_otp.html";
            }
        }else{
            map.put("falureemapass", "*in valed password chick the password");
            return "login.html";
        }
    }    
    }
    @GetMapping("/logout")
    public String logout(HttpSession session,ModelMap map) {
        session.removeAttribute("user");
        map.put("successmain", "logout successfully");
        return "home.html";
    }
    
    @GetMapping("/insert")
    public String insert(HttpSession session,ModelMap map) {
        if (session.getAttribute("user")!=null) {
            return "insert.html";
        }else{
            map.put("falureemapass", "* invalid user");
        return "login.html";
        }
    }

    @PostMapping("/insert")
    public String insertdata(StudentData studentData,HttpSession session,ModelMap map,@RequestParam MultipartFile stdpicher) {
        if (session.getAttribute("user")!=null) {
            studentData.setPicher(addToCloudinary(stdpicher));
            myStudentReposetry.save(studentData);
            map.put("success", "data enter succesfully");
            return "home.html";
        }else{
            map.put("falureemapass", "* invalid user");
            return "login.html";
        }
    }
    public String addToCloudinary(MultipartFile image) {
		Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", "dlwsojb20", "api_key",
				"946261358726857", "api_secret", "Yoi-AYk377aPh88hsEyOV8awM88", "secure", true));
		Map resume = null;
		try {
			Map<String, Object> uploadOptions = new HashMap<String, Object>();
			uploadOptions.put("folder", "Std Pictures");
			resume = cloudinary.uploader().upload(image.getBytes(), uploadOptions);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (String) resume.get("url");
	}
    
    
    @GetMapping("/fetch")
    public String fech(HttpSession session,ModelMap map) {
        if (session.getAttribute("user")!=null) {
            List<StudentData> list=myStudentReposetry.findAll();
            if (list.isEmpty()) {
                map.put("falure","no data found");
                return "home.html";
            }else{
                map.put("list",list);
                return "fech.html";
            }
        }else{
            map.put("falureemapass", "* invalid user");
        return "login.html";
        }
    }

    @GetMapping("/update")
    public String update(HttpSession session,ModelMap map ,@RequestParam int id) {
        if (session.getAttribute("user")!=null) {
            map.put("id", id);
            return "update.html";
        }else{
            map.put("falureemapass", "* invalid user");
        return "login.html";
        }
    }
    @PostMapping("/update")
    public String updatedetals(StudentData studentData,HttpSession session,ModelMap map,@RequestParam MultipartFile stdpicher) {
        if (session.getAttribute("user")!=null) {
            studentData.setPicher(addToCloudinary(stdpicher));
            myStudentReposetry.save(studentData);
            map.put("success", "data enter succesfully");
            List<StudentData> list=myStudentReposetry.findAll();
            if (list.isEmpty()) {
                map.put("falure","no data found");
                return "home.html";
            }else{
                map.put("list",list);
                return "fech.html";
            }
        }else{
            map.put("falureemapass", "* invalid user");
            return "login.html";
        }
    }
    

    @GetMapping("/delete")
    public String delete(HttpSession session,ModelMap map,@RequestParam int id) {
        if (session.getAttribute("user")!=null) {
         StudentData std=myStudentReposetry.findById(id).orElse(null);
          if (std==null) {
            map.put("falure", "data not found");
            return "fech.html";
          }else{
            map.put("succese", "data deleted");
            myStudentReposetry.deleteById(id);
            List<StudentData> list=myStudentReposetry.findAll();
            if (list.isEmpty()) {
                map.put("falure","no data found");
                return "home.html";
            }else{
                map.put("list",list);
                return "fech.html";
            }
          }
        }else{
            map.put("falureemapass", "* invalid user");
        return "login.html";
        }
    }
    

}
