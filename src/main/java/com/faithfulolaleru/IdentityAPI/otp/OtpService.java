package com.faithfulolaleru.IdentityAPI.otp;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
//@Slf4j
public record OtpService(OtpRepository otpRepository) {

    public boolean save(OtpEntity entity) {
        try {
            otpRepository.save(entity);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
