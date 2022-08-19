package com.example.carrentalservice.security.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
@Configuration
public class CustomOAuth2Service extends DefaultOAuth2UserService {
    // return custom OAuth2 user object.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return new CustomOAuth2(oAuth2User);
    }
}
