package com.sokhamart.platform.web.error;

public abstract class ConflictException extends BusinessException {
    public ConflictException(String message) {
        super(message);
    }
}
