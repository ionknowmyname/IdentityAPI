package com.faithfulolaleru.IdentityAPI.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppResponse<T> {

    private int statusCode;
    private HttpStatus httpStatus;
    private String message;
    private T data;
}
