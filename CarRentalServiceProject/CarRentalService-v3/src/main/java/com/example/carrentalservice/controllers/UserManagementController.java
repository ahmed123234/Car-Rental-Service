package com.example.carrentalservice.controllers;

import com.example.carrentalservice.configuration.exception.ApiRequestException;
import com.example.carrentalservice.models.entities.AppUser;
import com.example.carrentalservice.configuration.authentication.AppUserRole;
import com.example.carrentalservice.models.handelers.RestResponse;
import com.example.carrentalservice.repositories.AppUserRepository;
import com.example.carrentalservice.services.AppUserService;
import com.example.carrentalservice.models.handelers.RegistrationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@Transactional
@RequestMapping("/users/api/v1/")
public class UserManagementController {

   private final AppUserService appUserService;
   private final BCryptPasswordEncoder bCryptPasswordEncoder;
   private final AppUserRepository appUserRepository;


    @GetMapping("/validate")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public RestResponse getUser(Principal principal, @RequestBody String username, String password) {

        String message;

        String role = appUserService.getUserRole(principal);

        if (role.equals("CUSTOMER") || role.equals(("MANAGER"))){
            throw new ApiRequestException("Access denied", HttpStatus.FORBIDDEN);
        }

        message = appUserService.getUser(username,password);

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }

    @GetMapping("/role/{role}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public Optional<List<AppUser>> getByUserRole(Principal principal, @PathVariable("role") AppUserRole appUserRole) {

        String role = appUserService.getUserRole(principal);

        if (role.equals("CUSTOMER") || role.equals(("MANAGER"))) {
            throw new ApiRequestException("Access denied", HttpStatus.FORBIDDEN);
        }

        return appUserService.getByUserRole(appUserRole);
    }

    @GetMapping
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<AppUser> getUsers(Principal principal) {

        String role = appUserService.getUserRole(principal);

        if (role.equals("CUSTOMER") || role.equals(("MANAGER"))) {
            throw new ApiRequestException("Access denied", HttpStatus.FORBIDDEN);
        }

        return appUserService.getUsers();
    }

    @PutMapping("/update/status")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public RestResponse updateUserStatus(Principal principal, @RequestBody Long userId, boolean status) {

        String message;
        String role = appUserService.getUserRole(principal);

        if (role.equals("CUSTOMER") || role.equals(("MANAGER"))) {
            throw new ApiRequestException("Access denied", HttpStatus.FORBIDDEN);
        }

        message = appUserService.changeStatus(userId, status);

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }


    @DeleteMapping("/delete")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public RestResponse deleteAppUser(Principal principal, Long userId) {

        String message;

        String role = appUserService.getUserRole(principal);

        if (role.equals("CUSTOMER") || role.equals(("MANAGER"))) {

            UserDetails loginUser = (UserDetails) ((Authentication) principal).getPrincipal();

            String username = loginUser.getUsername();
            Long loginUserId =  appUserRepository.findByUsername(username).get().getUserId();

            message =  appUserService.deleteUser(loginUserId);
        }
        else
            message =  appUserService.deleteUser(userId);

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }


    @PostMapping("/add")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public RestResponse addAppUser(Principal principal, @RequestBody RegistrationRequest registrationRequest) {

        String message;

        String role = appUserService.getUserRole(principal);

        if (role.equals("CUSTOMER") || role.equals(("MANAGER"))) {
            throw new ApiRequestException("Access denied", HttpStatus.FORBIDDEN);
        }

        appUserService.addUser(registrationRequest);
        message = "User added successfully";

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }

    @PutMapping("{password}")
    public RestResponse updatePassword(Principal principal, @PathVariable("password")String password) {

        String message;
        String encodedPassword = bCryptPasswordEncoder.encode(password);

        message = appUserService.updateUserPassword(principal,  encodedPassword);

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }

}
