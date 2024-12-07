package com.katabank.exception;

import org.springframework.http.HttpStatus;

public class AccountException extends BasicApiException {
    public AccountException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    public AccountException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
