package com.example.carrentalservice.configuration.security.configuration;

import com.example.carrentalservice.services.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final AppUserService appUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final CustomOAuth2Service oAuth2Service;
    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable(); // send the post request without bean rejected.
        http.authorizeRequests().antMatchers("/").permitAll();
        http.authorizeRequests().antMatchers( "/registration/api/v*/**").permitAll();
//                .antMatchers("/management/api/v*/users/**").permitAll()
//        http.authorizeRequests().antMatchers("/cars/api/v*/**").hasRole("ADMIN");
//                .antMatchers(HttpMethod.DELETE, "/cars/api/v*/**").hasAuthority(CAR_WRITE.getPermission())
//                .antMatchers(HttpMethod.GET, "/cars/api/v*/**").hasAuthority(CAR_WRITE.getPermission())

        http.authorizeRequests().anyRequest()
                .authenticated().and()
                .httpBasic().and()
                .formLogin().and().oauth2Login().userInfoEndpoint().userService(oAuth2Service).and().successHandler(oAuth2LoginSuccessHandler);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(appUserService);
        return provider;
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }
}
