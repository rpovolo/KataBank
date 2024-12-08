package com.katabank.config;

import com.katabank.dto.ErrorDTO;
import com.katabank.exception.BasicApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ExceptionHandlerConfig {

    public static final String PREFIX_ERROR_CODE = "KATA-BANK-ERROR-";


    @ExceptionHandler(value = BasicApiException.class)
    public ResponseEntity<Object> handleAllUncaughtException(
            BasicApiException ex,
            WebRequest request) {

        return generateBody(ex, ex.getStatus());
    }

    private ResponseEntity<Object> generateBody(Exception ex, HttpStatus statusCode) {
        return new ResponseEntity<>(
                new ErrorDTO(
                        PREFIX_ERROR_CODE + String.format("%05d", statusCode.value()),
                        ex.getMessage()
                ),
                statusCode
        );
    }
}
