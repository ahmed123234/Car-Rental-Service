package com.example.carrentalservice.services.registration;

import com.example.carrentalservice.models.handelers.RegistrationRequest;
import javax.transaction.Transactional;

public interface RegistrationService {

    String register(RegistrationRequest registrationRequest);

    @Transactional
    String confirmToken(String token);
}
