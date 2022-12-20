package com.faithfulolaleru.IdentityAPI.utils;

import com.faithfulolaleru.IdentityAPI.exception.ErrorResponse;
import com.faithfulolaleru.IdentityAPI.exception.GeneralException;
import org.springframework.http.HttpStatus;

import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static String generateOtp() {
        SecureRandom rnd = new SecureRandom();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }


    /**
     * @param pinCode - validates 6 digit pin
     * @return true if code is 6 digits number
     */
    public static boolean validatePin(String pinCode) {
        final Pattern p = Pattern.compile("(\\d{6})");
        final Matcher m = p.matcher(pinCode);
        return m.find();
    }

    public static boolean validatePhoneNumber(String msisdn) {
        String regex = "^\\+(?:[0-9] ?){6,14}[0-9]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(msisdn);
        return matcher.matches();
    }

    public static String normalisePhoneNumber(String phoneNumber) {
        StringBuilder sb = new StringBuilder(phoneNumber);
        if (phoneNumber.startsWith("+234") || phoneNumber.startsWith("+")) {
            sb.replace(0, 4, "0");
            return sb.toString();
        }
        if (phoneNumber.startsWith("234")) {
            sb.replace(0, 3, "0");
            return sb.toString();
        } else {
            return phoneNumber;
        }
    }

    public static String toMsisdn(String phoneNumber) {
        phoneNumber = phoneNumber.replaceAll("[^0-9]", "");   // removes + at start

        if(phoneNumber.matches("^0[7-9][0-1]\\d{8}$")) {
            String last10Digits = phoneNumber.substring(phoneNumber.length() - 10);
            return "+234" + last10Digits;
        } else if(phoneNumber.startsWith("234") && phoneNumber.length() == 13) {
            return "+" + phoneNumber;
        } else {
            throw new GeneralException(HttpStatus.BAD_REQUEST,
                    ErrorResponse.ERROR_PHONE_NUMBER,
                    "Please Check the Phone number again");
        }
    }

    // boolean isValid = phoneNumber.matches("^0[7-9][0-1]\\d{8}$");
}
