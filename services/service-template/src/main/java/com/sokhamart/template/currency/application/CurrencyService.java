package com.sokhamart.template.currency.application;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sokhamart.template.currency.domain.Currency;
import com.sokhamart.template.currency.domain.CurrencyRepository;
import com.sokhamart.template.currency.domain.DuplicateCurrencyCodeException;

@Service
public class CurrencyService {
    private final CurrencyRepository repository;

    public CurrencyService(CurrencyRepository repository) {
        this.repository = repository;
    }

    public Currency create(String code, String name, String symbol) {
        Optional<Currency> existingCurrency = repository.findByCode(code);
        if (existingCurrency.isPresent()) {
            throw new DuplicateCurrencyCodeException("Currency code already exists: " + code);
        }
        return repository.save(new Currency(code, name, symbol));
    }

    public List<Currency> findAll() {
        return repository.findAll();
    }
}
