package com.faithfulolaleru.IdentityAPI.registration;

import com.faithfulolaleru.IdentityAPI.appUser.AppUserService;
import com.faithfulolaleru.IdentityAPI.dto.RegistrationRequest;
import com.faithfulolaleru.IdentityAPI.dto.RegistrationResponse;
import com.faithfulolaleru.IdentityAPI.response.AppResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/register")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    private final static String responseMessage = "App user token is %s";


    @PostMapping("/")
    public AppResponse<?> registerAppUser(@RequestBody RegistrationRequest requestDto) {

        String response = registrationService.registerAppUser(requestDto);

        return AppResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .httpStatus(HttpStatus.CREATED)
                .message(String.format(responseMessage, response))
                .build();
    }

    @PostMapping("/validateOtp")
    public AppResponse<?> validateOtp(@RequestParam("otp") String otp,
                                      @RequestParam("userId") Long userId) {

        String response = registrationService.validateOtp(otp, userId);

        return AppResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message(response)
                .build();
    }
}
