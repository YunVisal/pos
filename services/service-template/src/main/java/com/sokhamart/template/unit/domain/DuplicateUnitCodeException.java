package com.sokhamart.template.unit.domain;

import com.sokhamart.platform.web.error.ConflictException;

public class DuplicateUnitCodeException extends ConflictException {
    public DuplicateUnitCodeException(String message) {
        super(message);
    }
}
