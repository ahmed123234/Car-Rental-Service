package com.example.carrentalservice.security.configuration;

import com.example.carrentalservice.AppUser.AppUser;
import com.example.carrentalservice.AppUser.AppUserRepository;
import com.example.carrentalservice.AppUser.AppUserService;
import com.example.carrentalservice.AppUser.AuthenticationProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@AllArgsConstructor
@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final AppUserRepository appUserRepository;
    private final AppUserService appUserService;
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
        boolean isFound = appUserRepository.findByEmail(email).isPresent();

        if (!isFound){
            // register
            appUserService.CreateNewUserAfterOAuthLoginSuccess(email, firstName, lastName, AuthenticationProvider.GOOGLe);
            System.out.println(isFound);
        }else {
            appUserService.updateUserInfo(email, AuthenticationProvider.GOOGLe);
        }


        super.onAuthenticationSuccess(request, response, authentication);
    }
}
