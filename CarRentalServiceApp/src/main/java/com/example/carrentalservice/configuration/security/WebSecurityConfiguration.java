package com.example.carrentalservice.configuration.security;

import com.example.carrentalservice.filter.CustomAuthenticationFilter;
import com.example.carrentalservice.filter.CustomAuthorizationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
//    private final PasswordEncoder passwordEncoder;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final CustomOAuth2Service oAuth2Service;
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        CustomAuthenticationFilter customAuthenticationFilter =
                new CustomAuthenticationFilter(authenticationManagerBean());

        // override /login with your own custom authentication filter
         customAuthenticationFilter.setFilterProcessesUrl("/api/login");

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers( "/api/login/**", "/api/v*/token/refresh/**"
                ,"/api/v*/registration/**", "/api/v*/registration/confirm/**" , "/api/v*/users/token/refresh").permitAll();
        http.authorizeRequests().antMatchers(GET, "/api/v*/users/**")
                .hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST, "/api/v*/users/add/**")
                .hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(PUT, "/api/v*/users//update/status/**")
                .hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/api/v*/users/delete/**")
                .hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(PUT, "/api/users/update/password/**")
                .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER", "ROLE_MANAGER");

        http.authorizeRequests().antMatchers(GET, "/api/v*/cars/**")
                .hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER");

        http.authorizeRequests().antMatchers(GET, "/api/v*/cars/get/**")
                .hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER", "ROLE_CUSTOMER");

        http.authorizeRequests().antMatchers(GET, "/api/v*/cars/available/**")
                .hasAuthority("ROLE_CUSTOMER");

        http.authorizeRequests().antMatchers(POST, "/api/v*/cars/add/**")
                .hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER");

        http.authorizeRequests().antMatchers(PUT, "/api/v*/cars/update/**")
                .hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER");

        http.authorizeRequests().antMatchers(DELETE, "/api/v*/cars/delete/**")
                .hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER");



        http.authorizeRequests().anyRequest().authenticated();
//        http.httpBasic().and().formLogin().and().oauth2Login().userInfoEndpoint().userService(oAuth2Service)
//                .and().successHandler(oAuth2LoginSuccessHandler);

        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
