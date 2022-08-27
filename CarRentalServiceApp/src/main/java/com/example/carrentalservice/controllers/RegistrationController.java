package com.example.carrentalservice.controllers;

import com.example.carrentalservice.models.handelers.RestResponse;
import com.example.carrentalservice.models.handelers.RegistrationRequest;
import com.example.carrentalservice.models.handelers.regex_validation.RegistrationValidation;
import com.example.carrentalservice.services.registration.RegistrationServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestController
@RequestMapping(path = "/api/v1/registration")
@AllArgsConstructor
public class RegistrationController{

    private RegistrationServiceImpl registrationServiceImpl;
    private RegistrationValidation registrationValidation;


    @PostMapping
    public RestResponse register(@RequestBody RegistrationRequest registrationRequest){

        registrationValidation.validateUserInfo(registrationRequest.getEmail(),
                registrationRequest.getUserName(),
                registrationRequest.getPassword()
        );

        String message = "User added successfully";
        String link = registrationServiceImpl.register(registrationRequest);

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);
        objectNode.put("link", link);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }


    @GetMapping(path = "/confirm")
    public RestResponse confirm(@RequestParam("token") String token) {

        String message  = registrationServiceImpl.confirmToken(token);
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

    }
}

// TODO: can take: hasRole('ROLE_'), hasAnyRole('ROLE_'), hasAuthority('permission'),hasAnyAuthority('permission')
