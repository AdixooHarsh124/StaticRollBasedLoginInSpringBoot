package com.registration.Controllers;

import com.registration.Entities.Registration;
import com.registration.services.CustomUserDetailsService;
import com.registration.services.loginService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SecurityRequirement(name = "javainuseapi")
@RequiredArgsConstructor
@RestController
public class MainController {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private loginService LoginService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Registration registration) throws Exception {
        String emailRegex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(registration.getEmail());
        System.out.println("email "+registration.getEmail());
        if(!matcher.matches())
        {
            throw new Exception("email format wrong ");
        }

        String passwordValidation="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern patternn = Pattern.compile(passwordValidation);
        Matcher matcherr = patternn.matcher(registration.getPassword());
        System.out.println("password "+registration.getPassword());
        if(!matcherr.matches())
        {
            throw new Exception("must include a capital alphabet and a small, a number and special character,");
        }

        if(matcher.matches() && matcherr.matches()){
            return LoginService.login(registration.getEmail(), registration.getPassword());
        }else {
            System.out.println("else");
            throw new UsernameNotFoundException("user credential wrong");
        }
    }

    @GetMapping("/home")
    public String welcome()
    {
        String text="this is private page";
        text+="this page is not allowed to unauthenticated user";
        return text;
    }

    @PostMapping("/register")
    public Registration register(@RequestBody Registration registration) throws Exception {
        Registration reg;

        String nameRegex = "^[A-Za-z ]{3,30}$";
        Pattern pattername = Pattern.compile(nameRegex);
        Matcher matchername = pattername.matcher(registration.getFirstname());
        if (!matchername.matches()){
            throw new Exception("name is too short");
        }

        String emailRegex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(registration.getEmail());
        if(!matcher.matches())
        {
            throw new Exception("email format wrong ");
        }

        String passwordValidation="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern patternn = Pattern.compile(passwordValidation);
        Matcher matcherr = patternn.matcher(registration.getPassword());
        if(!matcherr.matches())
        {
            throw new Exception("must include a capital alphabet and a small, a number and special character,");
        }

        String mobileValidation="[6789][0-9]{9}";
        Pattern patternnn = Pattern.compile(mobileValidation);
        Matcher matcherrr = patternnn.matcher(registration.getMobile());
        if(!matcherr.matches())
        {
            throw new Exception("please enter right number format");
        }
        if(matcher.matches() && matcherr.matches() && matcherrr.matches() && matchername.matches()){
            registration.setPassword(this.bCryptPasswordEncoder.encode(registration.getPassword()));
            reg=customUserDetailsService.addUser(registration);
            return reg;
        }else {
            throw new UsernameNotFoundException("user enter wrong format values");
        }


    }





}
