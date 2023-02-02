package com.faithfulolaleru.IdentityAPI.login;

import com.faithfulolaleru.IdentityAPI.appUser.AppUserEntity;
import com.faithfulolaleru.IdentityAPI.appUser.AppUserService;
import com.faithfulolaleru.IdentityAPI.config.jwt.JwtService;
import com.faithfulolaleru.IdentityAPI.config.rabbitmq.RabbitMQProducer;
import com.faithfulolaleru.IdentityAPI.dto.LoginRequest;
import com.faithfulolaleru.IdentityAPI.dto.LoginResponse;
import com.faithfulolaleru.IdentityAPI.exception.ErrorResponse;
import com.faithfulolaleru.IdentityAPI.exception.GeneralException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
// @AllArgsConstructor
public record LoginService(JwtService jwtService, AppUserService appUserService,
           AuthenticationManager authenticationManager, RabbitMQProducer rabbitMQProducer) {

    public LoginResponse loginAppUser(LoginRequest requestDto) {

        Authentication authentication;
        try{
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    requestDto.getEmail(),
                    requestDto.getPassword()
                )
            );
        } catch (AuthenticationException ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();

            throw new GeneralException(
                HttpStatus.BAD_REQUEST,
                ErrorResponse.ERROR_APP_USER,
                "Invalid User Credentials"
            );
        }

        // AppUserEntity appUser = appUserService.loadUserByUsername(requestDto.getEmail());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        LoginResponse loginResponse = jwtService.generateToken(authentication);

        // send rabbitmq message to queue
        rabbitMQProducer.sendMessage(String.format("Token of loggedIn user --> %s", loginResponse.getToken()));
        rabbitMQProducer.sendMessage2(loginResponse);

        return loginResponse;
    }
}
