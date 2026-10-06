package com.sokhamart.template.currency.domain;

import java.util.List;
import java.util.Optional;

public interface CurrencyRepository {
    Currency save(Currency currency);

    Optional<Currency> findByCode(String code);

    List<Currency> findAll();
}
