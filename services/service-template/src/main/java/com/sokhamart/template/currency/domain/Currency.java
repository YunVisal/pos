package com.sokhamart.template.currency.domain;

public record Currency(String code, String name, String symbol) {
    public Currency {
        if (code == null) {
            throw new IllegalArgumentException("Code should not be null");
        }

        if (!code.matches("^[A-Z]{3}")) {
            throw new IllegalArgumentException("Currency code must be 3 uppercase letters (ISO 4217)");
        }

        if (name == null) {
            throw new IllegalArgumentException("Name should not be null");
        }

        if (name.isBlank()) {
            throw new IllegalArgumentException("Name should not be blank");
        }

        if (symbol == null) {
            throw new IllegalArgumentException("Symbol should not be null");
        }

        if (symbol.isBlank()) {
            throw new IllegalArgumentException("Symbol should not be blank");
        }
    }
}
