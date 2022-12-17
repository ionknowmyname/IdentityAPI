package com.faithfulolaleru.IdentityAPI.registration;

import com.faithfulolaleru.IdentityAPI.appUser.AppUserService;
import com.faithfulolaleru.IdentityAPI.dto.RegistrationRequest;
import com.faithfulolaleru.IdentityAPI.dto.RegistrationResponse;
import com.faithfulolaleru.IdentityAPI.response.AppResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/register")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;


    @PostMapping("/api/v1/register")
    public AppResponse<?> registerAppUser(@RequestBody RegistrationRequest requestDto) {

        RegistrationResponse response = registrationService.registerAppUser(requestDto);

        return AppResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .httpStatus(HttpStatus.CREATED)
                .data(response)
                .build();
    }
}
