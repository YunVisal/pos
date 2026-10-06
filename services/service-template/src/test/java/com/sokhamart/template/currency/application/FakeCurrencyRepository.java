package com.sokhamart.template.currency.application;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.sokhamart.template.currency.domain.Currency;
import com.sokhamart.template.currency.domain.CurrencyRepository;

class FakeCurrencyRepository implements CurrencyRepository {
    private final Map<String, Currency> currencies = new ConcurrentHashMap<>();

    @Override
    public Currency save(Currency currency) {
        currencies.put(currency.code(), currency);
        return currency;
    }

    @Override
    public Optional<Currency> findByCode(String code) {
        return Optional.ofNullable(currencies.get(code));
    }

    @Override
    public List<Currency> findAll() {
        return List.copyOf(currencies.values());
    }
}
