package com.registration.Repository;

import com.registration.Entities.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;


@EnableJpaRepositories
public interface RegistrationRepository extends JpaRepository<Registration,String> {
    @Query("SELECT email FROM Registration r WHERE r.email= :email")
    public Registration getUserByUserEmail(@Param("email") String email);

}