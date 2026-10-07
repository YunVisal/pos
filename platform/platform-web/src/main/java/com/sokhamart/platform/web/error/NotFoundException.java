package com.sokhamart.platform.web.error;

public abstract class NotFoundException extends BusinessException {
    public NotFoundException(String message) {
        super(message);
    }
}
