package com.faithfulolaleru.IdentityAPI.registration;

import com.faithfulolaleru.IdentityAPI.appUser.AppUserEntity;
import com.faithfulolaleru.IdentityAPI.appUser.AppUserRole;
import com.faithfulolaleru.IdentityAPI.appUser.AppUserService;
import com.faithfulolaleru.IdentityAPI.dto.RegistrationRequest;
import com.faithfulolaleru.IdentityAPI.dto.RegistrationResponse;
import com.faithfulolaleru.IdentityAPI.exception.ErrorResponse;
import com.faithfulolaleru.IdentityAPI.exception.GeneralException;
import com.faithfulolaleru.IdentityAPI.utils.EmailValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private EmailValidator emailValidator;

    private final AppUserService appUserService;

    public RegistrationResponse registerAppUser(RegistrationRequest request) {

        boolean emailValidated = emailValidator.test(request.getEmail());
        if(!emailValidated) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_INVALID_EMAIL, "Email not Valid");
        }

        String otp = appUserService.signUpAppUser(
                AppUserEntity.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .email(request.getEmail())
                        .phoneNumber(request.getPhoneNumber())
                        .password(request.getPassword())
                        .appUserRole(AppUserRole.USER)
                        .build()
        );


    }
}
