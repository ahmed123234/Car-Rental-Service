package com.example.carrentalservice.filter;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.intercept.RunAsImplAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.jaas.DefaultJaasAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

class CustomAuthenticationFilterTest {

    /**
     * Method under test: {@link CustomAuthenticationFilter#attemptAuthentication(HttpServletRequest, HttpServletResponse)}
     */
    @Test
    void testAttemptAuthentication2() throws AuthenticationException {
        ArrayList<AuthenticationProvider> authenticationProviderList = new ArrayList<>();
        authenticationProviderList.add(new RunAsImplAuthenticationProvider());
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(
                new ProviderManager(authenticationProviderList));
        assertThrows(IllegalArgumentException.class,
                () -> customAuthenticationFilter.attemptAuthentication(null, new Response()));
    }

}

