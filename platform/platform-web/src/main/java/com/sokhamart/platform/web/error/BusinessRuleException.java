package com.sokhamart.platform.web.error;

public abstract class BusinessRuleException extends BusinessException {
    public BusinessRuleException(String message) {
        super(message);
    }
}
