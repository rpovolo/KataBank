package com.katabank.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InternalErrorException extends BasicApiException {
    public InternalErrorException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    public InternalErrorException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
