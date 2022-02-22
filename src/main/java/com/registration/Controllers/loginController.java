package com.registration.Controllers;

import com.registration.Entities.Registration;
import com.registration.services.loginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class loginController {

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
}
