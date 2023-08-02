package com.faithfulolaleru.IdentityAPI.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice()
public class GeneralExceptionHandler {  // extends ResponseEntityExceptionHandler


    @ExceptionHandler({ GeneralException.class })
    public ResponseEntity<Object> handleException(final GeneralException ex) {
        log.error("General Exception handled  --> {}", ex);

        ErrorResponse newResponse = new ErrorResponse(ex);
        return new ResponseEntity<>(newResponse, newResponse.getHttpStatus());
    }

}
