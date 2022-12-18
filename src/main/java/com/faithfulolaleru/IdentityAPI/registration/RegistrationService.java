package com.faithfulolaleru.IdentityAPI.registration;

import com.faithfulolaleru.IdentityAPI.appUser.AppUserEntity;
import com.faithfulolaleru.IdentityAPI.appUser.AppUserRole;
import com.faithfulolaleru.IdentityAPI.appUser.AppUserService;
import com.faithfulolaleru.IdentityAPI.dto.RegistrationRequest;
import com.faithfulolaleru.IdentityAPI.exception.ErrorResponse;
import com.faithfulolaleru.IdentityAPI.exception.GeneralException;
import com.faithfulolaleru.IdentityAPI.otp.OtpEntity;
import com.faithfulolaleru.IdentityAPI.otp.OtpService;
import com.faithfulolaleru.IdentityAPI.utils.EmailValidator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RegistrationService {

    private EmailValidator emailValidator;

    private final AppUserService appUserService;

    private final OtpService otpService;

    public String registerAppUser(RegistrationRequest request) {

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


        return otp;
    }

    @Transactional
    public String validateOtp(String otp, Long userId) {

        AppUserEntity foundAppUser = appUserService.findUserByUserId(userId);
        OtpEntity foundOtpEntity = otpService.findByOtpAndAppUser(otp, foundAppUser);

    }
}
