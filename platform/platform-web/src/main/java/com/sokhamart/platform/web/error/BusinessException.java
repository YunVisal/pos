package com.sokhamart.platform.web.error;

abstract class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}