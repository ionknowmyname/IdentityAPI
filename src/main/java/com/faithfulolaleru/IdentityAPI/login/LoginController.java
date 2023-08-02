package com.faithfulolaleru.IdentityAPI.login;

import com.faithfulolaleru.IdentityAPI.dto.LoginRequest;
import com.faithfulolaleru.IdentityAPI.dto.LoginResponse;
import com.faithfulolaleru.IdentityAPI.dto.RegistrationRequest;
import com.faithfulolaleru.IdentityAPI.response.AppResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/login")
@AllArgsConstructor
public class LoginController {

    private final LoginService loginService;


    @PostMapping("/")
    public AppResponse<?> loginAppUser(@RequestBody LoginRequest requestDto) {

        LoginResponse response = loginService.loginAppUser(requestDto);

        return AppResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .data(response)
                .build();
    }
}
