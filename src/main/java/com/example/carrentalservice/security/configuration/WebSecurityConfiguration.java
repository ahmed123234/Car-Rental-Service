package com.example.carrentalservice.security.configuration;

import com.example.carrentalservice.AppUser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.example.carrentalservice.AppUser.AppUserRole.ADMIN;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final AppUserService appUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    @Autowired
    private final CustomOAuth2Service oAuth2Service;
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable() // send the post request without bean rejected.
                .authorizeRequests()
                //.antMatchers("/").permitAll()
                .antMatchers("/api/v*/registration/**").permitAll()
                .antMatchers("/api/v*/users/management/**","/api/v*/cars/**").hasRole(ADMIN.name())
                .anyRequest()
                .authenticated().and()
                //.httpBasic().and()
                .formLogin().and().oauth2Login().userInfoEndpoint().userService(oAuth2Service).and().successHandler(oAuth2LoginSuccessHandler);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(appUserService);
        return provider;
    }
}
