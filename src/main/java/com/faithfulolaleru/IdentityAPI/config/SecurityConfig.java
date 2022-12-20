package com.faithfulolaleru.IdentityAPI.config;


import com.faithfulolaleru.IdentityAPI.appUser.AppUserService;
import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {  // extends WebSecurityConfigurerAdapter


    private final AppUserService appUserService;

    private PasswordEncoder passwordEncoder;



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {



        http.csrf()
            .disable()
            .authorizeRequests()
            .antMatchers("/api/v1/register/**").permitAll()   //.hasRole("ADMIN")   // .hasAuthority("ADMIN")
            //.antMatchers("/user/**").permitAll()  //.hasAnyRole("USER", "ADMIN")   //.hasAnyAuthority("USER", "ADMIN")("USER", "ADMIN")
            //.antMatchers("/login/**").permitAll()  //.anonymous()
            .anyRequest().authenticated()
            .and().httpBasic()
            .and().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);



        return http.build();
    }


    /*  SOLUTION 1

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration
                                       authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    */

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder,
                                                        AppUserService appUserService) throws Exception {

        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(appUserService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(appUserService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }

}