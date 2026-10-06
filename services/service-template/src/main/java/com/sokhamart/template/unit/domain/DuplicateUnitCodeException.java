package com.sokhamart.template.unit.domain;

public class DuplicateUnitCodeException extends RuntimeException {
    public DuplicateUnitCodeException() {
        super();
    }

    public DuplicateUnitCodeException(String message) {
        super(message);
    }
}
