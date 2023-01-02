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

    private final static String responseMessage = "App user token is '%s'";


    @PostMapping("/")
    public AppResponse<?> registerAppUser(@RequestBody RegistrationRequest requestDto) {

        String response = registrationService.registerAppUser(requestDto);

        return AppResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .httpStatus(HttpStatus.CREATED)
                .message(String.format(responseMessage, response))
                .build();
    }

    @PostMapping("/validateOtpEmail")
    public AppResponse<?> validateOtpEmail(@RequestParam("otp") String otp,
                                      @RequestParam("userEmail") String userEmail) {

        String response = registrationService.validateOtpEmail(otp, userEmail);

        return AppResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message(response)
                .build();
    }

    @PostMapping("/validateOtpSms")
    public AppResponse<?> validateOtpSms(@RequestParam("otp") String otp,
                                      @RequestParam("phoneNumber") String phoneNumber) {

        String response = registrationService.validateOtpSms(otp, phoneNumber);

        return AppResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message(response)
                .build();
    }
}
