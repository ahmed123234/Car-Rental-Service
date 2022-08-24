package com.example.carrentalservice;

import com.example.carrentalservice.configuration.authentication.AppUserRole;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@SpringBootApplication
@RestController
@RequestMapping("/")
public class CarRentalServiceApplication {

    @RequestMapping
    public String home() {
        return "Hello....";
    }

    public static void main(String[] args) {
        SpringApplication.run(CarRentalServiceApplication.class, args);
        //System.out.println(AppUserRole.ADMIN.getPermissions());
        System.out.println(AppUserRole.ADMIN.name());



//        UserDetails loginUser = (UserDetails) ((Authentication) principal).getPrincipal();
//
//        System.out.println(role);
//        System.out.println(loginUser.getAuthorities());

    }

//    UserDetails userDetails = User.builder().roles(appUser.getAppUserRole().name()).build();
//
//    //  appUser.setAppUserRole();


}
