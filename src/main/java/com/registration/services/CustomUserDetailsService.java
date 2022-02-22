package com.registration.services;

import com.registration.Entities.Registration;
import com.registration.Repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



import java.util.ArrayList;
import java.util.List;


@Service
public class CustomUserDetailsService implements UserDetailsService{


    @Autowired
    private RegistrationRepository registrationRepository;
    private Registration reg=null;
    int mob=1;
    int email=1;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        Registration registration =  getUser(username);
            return new User(username,registration.getPassword(),new ArrayList<>());
    }

    /**
     *
     *Get ALL Users
     *
     */

    public List<Registration> getAllUser()
    {
        List<Registration> regi=registrationRepository.findAll();

        return regi;
    }

    /**
     *
     *  Add New User in Mysqldb
     *
     */

    public Registration addUser(Registration registration) throws Exception
    {

        List<Registration> users=getAllUser();
        users.forEach(user->{
            if(user.getMobile().equals(registration.getMobile()))
            {
                try {
                    throw new Exception("this mobile number is"+registration.getMobile()+" already exist");
                } catch (Exception e) {
                    System.out.println("mobile already exist");
                    mob=0;
//
                }
            } if(user.getEmail().equals(registration.getEmail()))
            {
                try {
                    throw new Exception("this "+registration.getEmail()+" is already exist");
                } catch (Exception e) {
                    System.out.println("email already exist");
                    email=0;
                }
            }
        });
        try {
            reg = registrationRepository.save(registration);
        }catch(Exception e)
        {
            if(mob==0)
            {
               throw new Exception("mobile is already register");
            }
            if(email==0)
            {
                throw new Exception("email is already register");
            }
            System.out.println("please fill details in right way");
//            e.printStackTrace();
        }
        return reg;
    }

    /**
     *
     * Get a User by Email
     *
     **/

    public Registration getUser(String email)
    {
        List<Registration> users=registrationRepository.findAll();
        int result=0;
        for (Registration user : users)
        {
            if (user.getEmail().equals(email)) {
                user.setFirstname(user.getFirstname());
                user.setLastname(user.getLastname());
                user.setEmail(user.getEmail());
                user.setMobile(user.getMobile());
                user.setPassword(user.getPassword());
                result=1;
                return user;
            }
        }
        if(result==0){
        throw new UsernameNotFoundException("user not found!!!");
        }
        return null;
    }


    public Registration update(String email,Registration registration)
    {
        System.out.println("email "+email);
        Registration reg=getUser(email);
        System.out.println("registration email "+registration.getEmail());
        System.out.println("user "+getUser(email).equals(registration.getEmail()));
       if(getUser(email).getEmail().equals(registration.getEmail())==false)
       {
           System.out.println(" condition ");
          throw new UsernameNotFoundException("user does not exist ");
       }else{
           reg.setFirstname(registration.getFirstname());
           reg.setLastname(registration.getLastname());
           reg.setEmail(registration.getEmail());
           reg.setMobile(registration.getMobile());
           reg.setPassword(registration.getPassword());
          return registrationRepository.save(reg);
       }

    }

    public String deleteByEmail(String email) {
        System.out.println("delete BY email");
        Registration regi=getUser(email);
        if(getUser(email).getEmail().equals(email)==true)
        {
            registrationRepository.delete(regi);
            return "user delete successfully";

        }else {
            throw new UsernameNotFoundException("user doesn't found");
        }
    }
}
