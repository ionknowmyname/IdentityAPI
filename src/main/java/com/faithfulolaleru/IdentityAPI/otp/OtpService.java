package com.faithfulolaleru.IdentityAPI.otp;

import com.faithfulolaleru.IdentityAPI.appUser.AppUserEntity;
import com.faithfulolaleru.IdentityAPI.exception.ErrorResponse;
import com.faithfulolaleru.IdentityAPI.exception.GeneralException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
//@Slf4j
public class OtpService {

    private final OtpRepository otpRepository;


    public boolean save(OtpEntity entity) {
        try {
            OtpEntity foundEntity = otpRepository.findByEmailOtpAndSmsOtpAndAppUser(entity.getEmailOtp(),
                            entity.getSmsOtp(), entity.getAppUser()).orElse(null);

            // email already confirmed for appUser, don't save otp entity
            if (foundEntity != null && foundEntity.getConfirmedAt() != null) {
                return false;
            }

            // if entity exists & not confirmed, replace otp with new otp
            if (foundEntity != null && foundEntity.getConfirmedAt() == null) {
                foundEntity.setEmailOtp(entity.getEmailOtp());
                foundEntity.setSmsOtp(entity.getSmsOtp());

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

    public OtpEntity findByEmailOtpAndAppUser(String emailOtp, AppUserEntity appUser) {

        return otpRepository.findByEmailOtpAndAppUser(emailOtp, appUser)
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND,
                        ErrorResponse.ERROR_OTP, "User with Otp not found"));
    }

    public OtpEntity findBySmsOtpAndAppUser(String smsOtp, AppUserEntity appUser) {

        return otpRepository.findBySmsOtpAndAppUser(smsOtp, appUser)
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND,
                        ErrorResponse.ERROR_OTP, "User with Otp not found"));
    }

    public int setConfirmedAt(String otp) {
        return otpRepository.updateConfirmedAt(otp, LocalDateTime.now());
    }
}
