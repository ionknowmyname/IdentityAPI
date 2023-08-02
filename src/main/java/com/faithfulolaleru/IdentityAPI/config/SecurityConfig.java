package com.faithfulolaleru.IdentityAPI.config;


import com.faithfulolaleru.IdentityAPI.appUser.AppUserService;
import com.faithfulolaleru.IdentityAPI.config.jwt.JwtAuthFilter;
import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {


    private final AppUserService appUserService;

    private PasswordEncoder passwordEncoder;

    private final JwtAuthFilter jwtFilter;



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        /* http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/api/v1/register/**").permitAll()
                //.requestMatchers("/api/**").permitAll()
                .anyRequest().authenticated()
            )
            // .authenticationManager(authenticationManager(http, passwordEncoder, appUserService))
            .authenticationProvider(authenticationProvider())  // comment out manager & provider as needed
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        */


        http.csrf()
            .disable()
            .authorizeHttpRequests()
            .requestMatchers("/api/v1/register/**").permitAll()   //.hasRole("ADMIN")   // .hasAuthority("ADMIN")
            .requestMatchers("/api/v1/login/**").permitAll()  //.hasAnyRole("USER", "ADMIN")   //.hasAnyAuthority("USER", "ADMIN")("USER", "ADMIN")
            //.requestMatchers("/login/**").permitAll()  //.anonymous()
            .anyRequest().authenticated()
            .and().httpBasic()
            .and().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);



        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration
                                       authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }


    /*

        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder,
                                                            AppUserService appUserService) throws Exception {

            return http.getSharedObject(AuthenticationManagerBuilder.class)
                    .userDetailsService(appUserService)
                    .passwordEncoder(passwordEncoder)
                    .and()
                    .build();
        }

    */

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        // can also return regular AuthenticationProvider

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(appUserService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }

}