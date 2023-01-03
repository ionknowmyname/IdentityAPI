package com.faithfulolaleru.IdentityAPI.registration;

import com.faithfulolaleru.IdentityAPI.appUser.AppUserEntity;
import com.faithfulolaleru.IdentityAPI.appUser.AppUserRole;
import com.faithfulolaleru.IdentityAPI.appUser.AppUserService;
import com.faithfulolaleru.IdentityAPI.dto.RegistrationRequest;
import com.faithfulolaleru.IdentityAPI.email.EmailSender;
import com.faithfulolaleru.IdentityAPI.exception.ErrorResponse;
import com.faithfulolaleru.IdentityAPI.exception.GeneralException;
import com.faithfulolaleru.IdentityAPI.otp.OtpEntity;
import com.faithfulolaleru.IdentityAPI.otp.OtpService;
import com.faithfulolaleru.IdentityAPI.sms.SmsSender;
import com.faithfulolaleru.IdentityAPI.utils.EmailValidator;
import com.faithfulolaleru.IdentityAPI.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class RegistrationService {

    private EmailValidator emailValidator;

    private final AppUserService appUserService;

    private final OtpService otpService;

    private final EmailSender emailSender;

    private final SmsSender smsSender;


    public String registerAppUser(RegistrationRequest request) {

        boolean emailValidated = emailValidator.test(request.getEmail());
        if(!emailValidated) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_EMAIL, "Email not Valid");
        }

        Map<String, Object> response = appUserService.signUpAppUser(buildAppUserEntity(request));

        String emailOtp = response.get("emailOtp").toString();
        String smsOtp = response.get("smsOtp").toString();
        String userEmail = response.get("userEmail").toString();
        String firstName = response.get("firstname").toString();
        String phoneNumber = response.get("phoneNumber").toString();
        log.info("firstname --> ", firstName);



        String link  = "http://localhost:8080/api/v1/register/validateOtpEmail?otp=" + emailOtp + "&userEmail=" + userEmail;

        emailSender.send(userEmail, Utils.buildEmail(firstName, link));

        String message = "Kindly use " + smsOtp + " to validate.";
        smsSender.send(phoneNumber, message);

        return emailOtp;
    }

    @Transactional   // all or nothing for the two services called inside
    public String validateOtpEmail(String emailOtp, String userEmail) {

        AppUserEntity foundAppUser = appUserService.findUserByEmail(userEmail);
        OtpEntity foundOtpEntity = otpService.findByEmailOtpAndAppUser(emailOtp, foundAppUser);

        if(foundOtpEntity.getConfirmedAt() != null) {
            throw new GeneralException(HttpStatus.CONFLICT, ErrorResponse.ERROR_EMAIL,
                    "AppUser is already validated");
        }

        LocalDateTime expiresAt = foundOtpEntity.getExpiresAt();
        if(expiresAt.isBefore(LocalDateTime.now())) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_OTP,
                    "Otp is expired");
        }

        otpService.setConfirmedAt(emailOtp);

        appUserService.activateAppUser(foundOtpEntity.getAppUser().getEmail());

        return "Email Otp has been Validated";
    }

    @Transactional   // all or nothing for the two services called inside
    public String validateOtpSms(String smsOtp, String phoneNumber) {

        AppUserEntity foundAppUser = appUserService.findUserByPhoneNumber(phoneNumber);
        OtpEntity foundOtpEntity = otpService.findBySmsOtpAndAppUser(smsOtp, foundAppUser);

        if(foundOtpEntity.getConfirmedAt() != null) {
            throw new GeneralException(HttpStatus.CONFLICT, ErrorResponse.ERROR_PHONE_NUMBER,
                    "AppUser is already validated");
        }

        LocalDateTime expiresAt = foundOtpEntity.getExpiresAt();
        if(expiresAt.isBefore(LocalDateTime.now())) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_OTP,
                    "Otp is expired");
        }

        otpService.setConfirmedAt(smsOtp);

        appUserService.activateAppUser(foundOtpEntity.getAppUser().getEmail());

        return "Sms Otp has been Validated";
    }

    private AppUserEntity buildAppUserEntity(RegistrationRequest request) {
        return AppUserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(request.getPassword())
                .appUserRole(AppUserRole.USER)
                .build();
    }




}
