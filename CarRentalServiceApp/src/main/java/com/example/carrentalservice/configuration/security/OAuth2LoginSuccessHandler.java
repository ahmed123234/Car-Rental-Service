package com.example.carrentalservice.configuration.security;

import com.example.carrentalservice.configuration.security.AuthenticationProvider;
import com.example.carrentalservice.configuration.security.CustomOAuth2;
import com.example.carrentalservice.models.entities.AppUser;
import com.example.carrentalservice.repositories.AppUserRepository;
import com.example.carrentalservice.services.user.AppUserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final AppUserRepository appUserRepository;
    private final AppUserServiceImpl appUserServiceImpl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2 customOAuth2 = (CustomOAuth2) authentication.getPrincipal();
        String email = customOAuth2.getEmail();
        String name = customOAuth2.getName();
        String [] fullName = name.split(" ");
        String firstName = fullName[0];
        String lastName = fullName[1];
        System.out.println("User's email: " + email);
        System.out.println("User's password: " + customOAuth2.getPassword());
        System.out.println("User's name: " + firstName + " " + lastName);
        AppUser appUser = appUserRepository.findByEmail(email);

        if (appUser == null){
            // register
            appUserServiceImpl.createNewUserAfterOAuthLoginSuccess(email, firstName, lastName, AuthenticationProvider.GOOGLe);
        }else {
            appUserServiceImpl.updateUserInfo(email, AuthenticationProvider.GOOGLe);
        }


        super.onAuthenticationSuccess(request, response, authentication);
    }
}
