package com.sokhamart.template.currency.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CurrencyTest {
    @Test
    void testThrowIllegalArgumentExceptionWhenCodeIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Currency(null, "US Dollar", "$");
        });
    }

    @Test
    void testThrowIllegalArgumentExceptionWhenCodeIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Currency(" ", "US Dollar", "$");
        });
    }

    @Test
    void testThrowIllegalArgumentExceptionWhenCodeIsNotExactly3Chars() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Currency("us", "US Dollar", "$");
        });
    }

    @Test
    void testThrowIllegalArgumentExceptionWhenCodeIsNotUpperCase() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Currency("usd", "US Dollar", "$");
        });
    }

    @Test
    void testThrowIllegalArgumentExceptionWhenCodeContainSymbolOrNumber() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Currency("$$$", "US Dollar", "$");
        });
    }

    @Test
    void testCurrencyCode() {
        Currency currency = new Currency("USD", "US Dollar", "$");
        assertEquals("USD", currency.code(), "Should return USD");
    }

    @Test
    void testThrowIllegalArgumentExceptionWhenNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Currency("USD", null, "$");
        });
    }

    @Test
    void testThrowIllegalArgumentExceptionWhenNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Currency("USD", " ", "$");
        });
    }

    @Test
    void testCurrencyName() {
        Currency currency = new Currency("USD", "US Dollar", "$");
        assertEquals("US Dollar", currency.name(), "Should return US Dollar");
    }

    @Test
    void testThrowIllegalArgumentExceptionWhenSymbolIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Currency("USD", "US Dollar", null);
        });
    }

    @Test
    void testThrowIllegalArgumentExceptionWhenSymbolIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Currency("USD", "US Dollar", "");
        });
    }

    @Test
    void testCurrencySymbol() {
        Currency currency = new Currency("USD", "US Dollar", "$");
        assertEquals("$", currency.symbol(), "Should return $");
    }
}
