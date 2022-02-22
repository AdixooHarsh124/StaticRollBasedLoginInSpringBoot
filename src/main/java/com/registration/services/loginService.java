package com.registration.services;

import com.registration.Entities.Registration;
import com.registration.Repository.RegistrationRepository;
import com.registration.helper.JwtUtil;
import com.registration.model.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class loginService {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public ResponseEntity<?> login(String email,String password)
    {
        try{
            Registration registration=customUserDetailsService.getUser(email);
            boolean isPasswordMatch =bCryptPasswordEncoder.matches(password, registration.getPassword());
            if(isPasswordMatch!=true){
                throw new UsernameNotFoundException("user credentials wrong");
            }else{
            UserDetails userDetails=customUserDetailsService.loadUserByUsername(registration.getEmail());
            String token=this.jwtUtil.generateToken(userDetails);
              return ResponseEntity.ok(new JwtResponse(token));
            }
        }
        catch(Exception e){
            e.printStackTrace();
            throw new UsernameNotFoundException("user not found!!!");
        }

    }
}
