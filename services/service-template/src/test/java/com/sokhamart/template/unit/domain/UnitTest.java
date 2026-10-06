package com.sokhamart.template.unit.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class UnitTest {
    @Test
    void testUnitCode() {
        Unit unit = new Unit("KG", "Kilogram");
        assertEquals("KG", unit.code(), "Should return KG");
    }

    @Test
    void testThrowIllegalArgumentExceptionWhenCodeIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            Unit unit = new Unit(" ", "Kilogram");
        });
    }

    @Test
    void testThrowIllegalArgumentExceptionWhenCodeIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            Unit unit = new Unit(null, "Kilogram");
        });
    }

    @Test
    void testThrowIllegalArgumentExceptionWhenCodeIsNotUppercaseLetters() {
        assertThrows(IllegalArgumentException.class, () -> {
            Unit unit = new Unit("kg", "Kilogram");
        });
    }

    @Test
    void testThrowIllegalArgumentExceptionWhenCodeContainSpecialCharacter() {
        assertThrows(IllegalArgumentException.class, () -> {
            Unit unit = new Unit("k&g", "Kilogram");
        });
    }

    @Test
    void testThrowIllegalArgumentExceptionWhenCodeLongerThan10Chars() {
        assertThrows(IllegalArgumentException.class, () -> {
            Unit unit = new Unit("TOOLONGCODE1", "Kilogram");
        });
    }

    @Test
    void testThrowIllegalArgumentExceptionWhenNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            Unit unit = new Unit("KG", null);
        });
    }

    @Test
    void testThrowIllegalArgumentExceptionWhenNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            Unit unit = new Unit("KG", " ");
        });
    }
}
