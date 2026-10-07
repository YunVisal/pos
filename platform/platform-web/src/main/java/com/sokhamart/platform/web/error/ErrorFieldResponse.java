package com.sokhamart.platform.web.error;

import org.springframework.validation.FieldError;

public record ErrorFieldResponse(String field, String message) {
    static ErrorFieldResponse from(FieldError error) {
        return new ErrorFieldResponse(error.getField(), error.getDefaultMessage());
    }
}
