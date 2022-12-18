package com.faithfulolaleru.IdentityAPI.otp;

import com.faithfulolaleru.IdentityAPI.appUser.AppUserEntity;
import com.faithfulolaleru.IdentityAPI.exception.ErrorResponse;
import com.faithfulolaleru.IdentityAPI.exception.GeneralException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
//@Slf4j
public record OtpService(OtpRepository otpRepository) {

    public boolean save(OtpEntity entity) {
        try {
            OtpEntity foundEntity = otpRepository.findByOtpAndAppUser(entity.getOtp(), entity.getAppUser())
                    .orElse(null);

            // if entity exists & not confirmed, replace otp with new otp
            if(foundEntity != null && foundEntity.getConfirmedAt() == null) {
                foundEntity.setOtp(entity.getOtp());

                otpRepository.save(foundEntity);
                return true;
            }

            otpRepository.save(entity);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public OtpEntity findByOtpAndAppUser(String otp, AppUserEntity appUser) {

        return otpRepository.findByOtpAndAppUser(otp, appUser)
            .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND,
                        ErrorResponse.ERROR_USER_OTP_NOT_EXIST, "User with Otp not found"));
    }
}
