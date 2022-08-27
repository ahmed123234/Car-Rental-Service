package com.example.carrentalservice;

import com.example.carrentalservice.models.entities.AppUser;
import com.example.carrentalservice.models.entities.UserRole;
import com.example.carrentalservice.services.user.AppUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@SpringBootApplication
@RestController
@RequestMapping("/")
public class CarRentalServiceApplication {

    @RequestMapping
    public String home() {
        return "Hello....";
    }


    @Bean
    CommandLineRunner run(AppUserService userService) {
        return args -> {
            userService.saveRole(new UserRole(null, "ROLE_USER"));
            userService.saveRole(new UserRole(null, "ROLE_MANAGER"));
            userService.saveRole(new UserRole(null, "ROLE_ADMIN"));
            userService.saveRole(new UserRole(null, "ROLE_SUPER_ADMIN"));


            userService.saveUser(new AppUser("ali", "fares", "ali@gmail.com",
                    "ali12", "201712@Asg",new ArrayList<>()));

            userService.saveUser(new AppUser("ahmad", "sameer", "ahmad@gmail.com",
                    "ahmad12", "201712@Asg",new ArrayList<>()));

            userService.saveUser(new AppUser("akram", "ali", "akram@gmail.com",
                    "akram12", "201712@Asg",new ArrayList<>()));

            userService.saveUser(new AppUser("anwar", "ali", "anwar@gmail.com",
                    "anwar12", "201712@Asg",new ArrayList<>()));


            userService.addRoleToUser("ahmad@gmail.com", "ROLE_USER");
            userService.addRoleToUser("ahmad@gmail.com", "ROLE_MANAGER");
            userService.addRoleToUser("ali@gmail.com", "ROLE_MANAGER");
            userService.addRoleToUser("akram@gmail.com", "ROLE_ADMIN");
            userService.addRoleToUser("anwar@gmail.com", "ROLE_SUPER_ADMIN");
            userService.addRoleToUser("anwar@gmail.com", "ROLE_ADMIN");
            userService.addRoleToUser("anwar@gmail.com", "ROLE_USER");

        };
    }
    public static void main(String[] args) {
        SpringApplication.run(CarRentalServiceApplication.class, args);
    }

}
