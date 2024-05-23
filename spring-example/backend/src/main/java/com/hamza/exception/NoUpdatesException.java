package com.hamza.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoUpdatesException extends RuntimeException {
    public NoUpdatesException(String message) {
        super(message);
    }
}
