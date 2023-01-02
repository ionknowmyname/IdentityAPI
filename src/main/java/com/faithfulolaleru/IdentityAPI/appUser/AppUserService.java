package com.faithfulolaleru.IdentityAPI.appUser;

import com.faithfulolaleru.IdentityAPI.exception.ErrorResponse;
import com.faithfulolaleru.IdentityAPI.exception.GeneralException;
import com.faithfulolaleru.IdentityAPI.otp.OtpEntity;
import com.faithfulolaleru.IdentityAPI.otp.OtpService;
import com.faithfulolaleru.IdentityAPI.utils.EmailValidator;
import com.faithfulolaleru.IdentityAPI.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    private final PasswordEncoder passwordEncoder;
    // private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final OtpService otpService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
//                .map(appUser -> withUsername(appUser.getEmail())
//                        .password(appUser.getPassword())
//                        .authorities(getAuthorities(appUser))
//                        .credentialsExpired(false)
//                        .build())
                .orElseThrow(() -> new GeneralException(
                        HttpStatus.BAD_REQUEST,
                        ErrorResponse.ERROR_APP_USER,
                        "Invalid User Credentials"));

        // no need to map appUserEntity to userDetails coz now appUserEntity extends userDetails
    }

    public Map<String, Object> signUpAppUser(AppUserEntity entity) {

        boolean userExist = appUserRepository.existsByEmail(entity.getEmail());
        if(userExist) {
            // TODO check if attributes are the same and
            // TODO if email not confirmed send confirmation email.
            throw new GeneralException(HttpStatus.CONFLICT, ErrorResponse.ERROR_APP_USER,
                    "AppUser with email already exists");
        }

        entity.setPassword(passwordEncoder.encode(entity.getPassword()));

        AppUserEntity savedAppUser = appUserRepository.save(entity);


        // Create OTP for AppUser

        String emailOtp = UUID.randomUUID().toString();
        String smsOtp = Utils.generateOtp();

        OtpEntity otpEntity = OtpEntity.builder()
                .emailOtp(emailOtp)
                .smsOtp(smsOtp)
                .appUser(savedAppUser)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .build();

        boolean isSaved = otpService.save(otpEntity);

        if(!isSaved) {
            throw new GeneralException(HttpStatus.CONFLICT, ErrorResponse.ERROR_OTP,
                    "OTP was not saved Successfully");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("emailOtp", emailOtp);
        response.put("smsOtp", smsOtp);
        response.put("userEmail", savedAppUser.getEmail());
        response.put("firstname", savedAppUser.getFirstName());
        response.put("phoneNumber", savedAppUser.getPhoneNumber());

        return response;
    }

    public int activateAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }

    public AppUserEntity findUserByUserId(Long userId) {
        return appUserRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND,
                        ErrorResponse.ERROR_APP_USER, "User with id not found"));
    }

    private Collection<? extends SimpleGrantedAuthority> getAuthorities(AppUserEntity appUser) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(appUser.getAppUserRole().name()));

        return authorities;
    }

    public AppUserEntity findUserByEmail(String userEmail) {
        return appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND,
                        ErrorResponse.ERROR_APP_USER, "User with email not found"));
    }

    public AppUserEntity findUserByPhoneNumber(String phoneNumber) {
        return appUserRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND,
                        ErrorResponse.ERROR_APP_USER, "User with email not found"));
    }
}
