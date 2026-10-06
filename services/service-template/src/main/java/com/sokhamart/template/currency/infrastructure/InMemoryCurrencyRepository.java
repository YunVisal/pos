package com.sokhamart.template.currency.infrastructure;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.sokhamart.template.currency.domain.Currency;
import com.sokhamart.template.currency.domain.CurrencyRepository;

@Repository
class InMemoryCurrencyRepository implements CurrencyRepository {
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
