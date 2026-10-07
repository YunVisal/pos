package com.sokhamart.template.unit.domain;

import java.util.Locale;

public record Unit(String code, String name) {
    public Unit {
        if (code == null) {
            throw new IllegalArgumentException("Code should not be null.");
        }

        if (code.isBlank()) {
            throw new IllegalArgumentException("Code should not be blank.");
        }

        if (code.length() > 10) {
            throw new IllegalArgumentException("Code should be 1 to 10 characters long.");
        }

        if (!code.toUpperCase(Locale.ROOT).equals(code)) {
            throw new IllegalArgumentException("Code should be in uppercase characters.");
        }

        if (!code.matches("^[A-Z0-9]+$")) {
            throw new IllegalArgumentException("Code should contains only letters and number.");
        }

        if (name == null) {
            throw new IllegalArgumentException("Name should not be null.");
        }

        if (name.isBlank()) {
            throw new IllegalArgumentException("Name should not be blank");
        }

    }
}
