package com.example.carrentalservice.controllers.admin_controller;

import com.example.carrentalservice.AppUser.AppUser;
import com.example.carrentalservice.AppUser.AppUserRole;
import com.example.carrentalservice.AppUser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/users/management")
public class UserManagement {

   private final AppUserService appUserService;

    @GetMapping("/validateUser")
    public String getUser(@RequestBody String username, String password) {

        return appUserService.getUser(username,password);
    }

    @GetMapping("/role/{role}")
    public Optional<List<AppUser>> getByUserRole(@PathVariable("role") AppUserRole appUserRole) {
        return appUserService.getByUserRole(appUserRole);
    }



}
