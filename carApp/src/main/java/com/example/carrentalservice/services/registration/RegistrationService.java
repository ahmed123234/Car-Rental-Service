package com.example.carrentalservice.services.registration;

import com.example.carrentalservice.models.handelers.AppUserRequest;

import javax.transaction.Transactional;

public interface RegistrationService {

    String register(AppUserRequest appUserRequest);

    @Transactional
    String confirmToken(String token);
}
