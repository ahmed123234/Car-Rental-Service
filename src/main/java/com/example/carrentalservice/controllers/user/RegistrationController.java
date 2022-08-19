package com.example.carrentalservice.controllers.user;

import com.example.carrentalservice.registerationprocess.RegistrationRequest;
import com.example.carrentalservice.registerationprocess.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/registration")
@AllArgsConstructor
public class RegistrationController{

    private RegistrationService registrationService;

    @PostMapping
    public String register(@RequestBody RegistrationRequest registrationRequest){
        return registrationService.register(registrationRequest);
    }


    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}