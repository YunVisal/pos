package com.sokhamart.template.currency.domain;

public class DuplicateCurrencyCodeException extends RuntimeException {
    public DuplicateCurrencyCodeException() {
        super();
    }

    public DuplicateCurrencyCodeException(String message) {
        super(message);
    }
}
