package com.example.carrentalservice.controllers;

import com.example.carrentalservice.models.entities.AppUser;
import com.example.carrentalservice.configuration.authentication.AppUserRole;
import com.example.carrentalservice.services.AppUserService;
import com.example.carrentalservice.models.handelers.RegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/users/management")
public class UserManagementController {

   private final AppUserService appUserService;
   private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping("/validateUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getUser(@RequestBody String username, String password) {

        return appUserService.getUser(username,password);
    }

    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Optional<List<AppUser>> getByUserRole(@PathVariable("role") AppUserRole appUserRole) {
        return appUserService.getByUserRole(appUserRole);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<AppUser> getUsers() {
        return appUserService.getUsers();
    }

    @PutMapping("/update-status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateUserStatus(@RequestBody Long userId, boolean status) {

        return appUserService.changeStatus(userId, status);
    }
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteAppUser(Long userId) {
        return appUserService.deleteUser(userId);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addAppUser(@RequestBody RegistrationRequest registrationRequest) {
        appUserService.addUser(registrationRequest);
        return "User added successfully";
    }

    @PutMapping("{password}")
    public String updatePassword(Principal principal, @PathVariable("password")String password) {
        String encodedPassword = bCryptPasswordEncoder.encode(password);

        return appUserService.updateUserPassword(principal,  encodedPassword);
    }

}
