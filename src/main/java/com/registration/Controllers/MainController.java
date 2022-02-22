package com.registration.Controllers;

import com.registration.Entities.Registration;
import com.registration.services.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.regex.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class MainController {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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

    @GetMapping("/users")
    public List<Registration> users()
    {
        return customUserDetailsService.getAllUser();
    }

    @GetMapping("/user/{email}")
    public Registration updateUser(@PathVariable("email") String email)
    {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        System.out.println("matcher "+matcher.matches());
        if(matcher.matches())
        {
            return customUserDetailsService.getUser(email);
        }else{
            throw new UsernameNotFoundException("user does not exits");
        }
    }

    @PostMapping("/update/{email}")
    public Registration updateUser(@PathVariable("email") String email,@RequestBody Registration registration)
    {
        Registration reg;
        String emailRegex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(registration.getEmail());
        System.out.println("email "+matcher.matches());


        String passwordValidation="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern patternn = Pattern.compile(passwordValidation);
        Matcher matcherr = patternn.matcher(registration.getPassword());
        System.out.println("password "+matcherr.matches());


        String mobileValidation="[6789][0-9]{9}";
        Pattern patternnn = Pattern.compile(mobileValidation);
        Matcher matcherrr = patternnn.matcher(registration.getMobile());
        System.out.println("mobile "+matcherrr.matches());
        if(matcher.matches() && matcherr.matches() && matcherrr.matches()){
        registration.setPassword(this.bCryptPasswordEncoder.encode(registration.getPassword()));
        reg=customUserDetailsService.update(email, registration);
        return reg;
        }else {
            throw new UsernameNotFoundException("user enter wrong format values");
        }
    }

    @DeleteMapping("/delete/{email}")
    public String userDelete(@PathVariable("email") String email)
    {
        System.out.println("email");
        customUserDetailsService.deleteByEmail(email);
        return "success"+email;
    }




}
