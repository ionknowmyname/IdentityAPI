package com.faithfulolaleru.IdentityAPI.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {


    public static final String ERROR_CODE_INVALID_MSISDN = "INVALID_MSISDN";


    public static final String ERROR_USER_ALREADY_EXIST = "USER_ALREADY_EXISTS";
    public static final String ERROR_USER_NOT_EXIST = "USER_DOES_NOT_EXIST";

    public static final String ERROR_ROLE_NOT_EXIST = "ROLE_DOES_NOT_EXIST";
    public static final String ERROR_INVALID_USER_CREDENTIALS = "INVALID_USERNAME_PASSWORD";

    public static final String ERROR_INVALID_EMAIL = "INVALID_EMAIL";


    private String error;
    private String message;
    private HttpStatus httpStatus;


    public ErrorResponse(final GeneralException ex) {
        this.httpStatus = ex.getHttpStatus();
        this.error = ex.getError();
        this.message = ex.getMessage();   //  ex.getLocalizedMessage()
    }
}
