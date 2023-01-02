package com.faithfulolaleru.IdentityAPI.otp;

import com.faithfulolaleru.IdentityAPI.appUser.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity, Long> {


    Optional<OtpEntity> findByEmailOtpAndSmsOtpAndAppUser(String emailOtp, String smsOtp, AppUserEntity appUser);

    Optional<OtpEntity> findByEmailOtpAndAppUser(String emailOtp, AppUserEntity appUser);

    Optional<OtpEntity> findBySmsOtpAndAppUser(String smsOtp, AppUserEntity appUser);


    @Transactional
    @Modifying
    @Query("UPDATE otp_table c SET c.confirmedAt = ?2 WHERE c.emailOtp = ?1 OR c.smsOtp = ?1")
    int updateConfirmedAt(String otp, LocalDateTime confirmedTime);
}
