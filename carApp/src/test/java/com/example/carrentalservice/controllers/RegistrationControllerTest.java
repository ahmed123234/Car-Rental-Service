package com.example.carrentalservice.controllers;

import com.example.carrentalservice.models.handelers.AppUserRequest;
import com.example.carrentalservice.services.registration.RegistrationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ContextConfiguration(classes = {RegistrationController.class})
@ExtendWith(SpringExtension.class)
@WebMvcTest
class RegistrationControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private RegistrationController registrationController;

    @MockBean
    private RegistrationServiceImpl registrationServiceImpl;

    /**
     * Method under test: {@link RegistrationController#confirm(String)}
     */
    @Test
    void testConfirm() throws Exception {
        Mockito.when(registrationServiceImpl.confirmToken(anyString())).thenReturn("confirmed");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/registration/confirm").accept(MediaType.APPLICATION_JSON)).andReturn();

        System.out.println(mvcResult.getResponse());
        registrationController.confirm(anyString());
        Mockito.verify(registrationServiceImpl).confirmToken(anyString());
    }

    /**
     * Method under test: {@link RegistrationController#register(AppUserRequest)}
     */
    @Test
    void testRegister() throws Exception {
        Mockito.when(registrationServiceImpl.register(any())).thenReturn("User added successfully");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/registration").accept(MediaType.APPLICATION_JSON)).andReturn();

        System.out.println(mvcResult.getResponse());
        registrationController.register(new AppUserRequest("ahmad", "ali",
                "ahmad@gmail.com", "ahmad_1234", "201712@Asg", new String[]{"ROLE_ADMIN"}));

        Mockito.verify(registrationServiceImpl).register(any());
    }
}

