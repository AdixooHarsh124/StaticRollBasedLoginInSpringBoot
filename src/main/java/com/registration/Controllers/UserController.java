package com.registration.Controllers;

import com.registration.Entities.Registration;
import com.registration.helper.JwtUtil;
import com.registration.model.JwtRequest;
import com.registration.model.JwtResponse;
import com.registration.services.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SecurityRequirement(name = "javainuseapi")
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/token")
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception {

        try{
            System.out.println("try");
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),jwtRequest.getPassword()));
        }catch(UsernameNotFoundException e)
        {
            System.out.println("catch");
            e.printStackTrace();
            throw new Exception("Bad Credentials");
        }
        //fine area ....
        //than create token
        UserDetails userDetails=this.customUserDetailsService.loadUserByUsername(jwtRequest.getUsername());
        String token=this.jwtUtil.generateToken(userDetails);
        System.out.println("JWT :-"+token);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @GetMapping("/")
    public String getBaseUser(){
        return "user deshboard";
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

        String passwordValidation="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern patternn = Pattern.compile(passwordValidation);
        Matcher matcherr = patternn.matcher(registration.getPassword());

        String mobileValidation="[6789][0-9]{9}";
        Pattern patternnn = Pattern.compile(mobileValidation);
        Matcher matcherrr = patternnn.matcher(registration.getMobile());
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
