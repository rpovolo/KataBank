package com.katabank.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends BasicApiException {
    public NotFoundException() {
        super(HttpStatus.NOT_FOUND, null);
    }

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}