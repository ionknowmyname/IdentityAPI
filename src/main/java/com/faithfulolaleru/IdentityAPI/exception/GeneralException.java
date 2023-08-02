package com.faithfulolaleru.IdentityAPI.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class GeneralException extends RuntimeException {

    private final String error;
    private final HttpStatus httpStatus;


    public GeneralException(HttpStatus httpStatus, String error, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.error = error;
    }
}
