package com.sokhamart.template.currency.application;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.sokhamart.template.currency.domain.Currency;
import com.sokhamart.template.currency.domain.CurrencyRepository;
import com.sokhamart.template.currency.domain.DuplicateCurrencyCodeException;

class CurrencyServiceTest {
    @Test
    void testContainCreatedCurrency() {
        CurrencyRepository repository = new FakeCurrencyRepository();
        CurrencyService service = new CurrencyService(repository);
        service.create("USD", "US Dollar", "$");
        List<Currency> currencies = service.findAll();
        boolean contained = currencies.stream().anyMatch(obj -> "USD".equals(obj.code()));
        assertTrue(contained, "findAll() return the list that not contained the created currency");
    }

    @Test
    void testThrowDuplicateCurrencyCodeExceptionWhenTheSavedCodeIsAlreadyExist() {
        CurrencyRepository repository = new FakeCurrencyRepository();
        CurrencyService service = new CurrencyService(repository);
        service.create("USD", "US Dollar", "$");
        assertThrows(DuplicateCurrencyCodeException.class, () -> {
            service.create("USD", "US Dollar", "$");
        });
    }
}
