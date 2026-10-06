package com.sokhamart.template.currency.api;

import com.sokhamart.template.currency.domain.Currency;

public record CurrencyResponse(String code, String name, String symbol) {
    static CurrencyResponse from(Currency currency) {
        return new CurrencyResponse(currency.code(), currency.name(), currency.symbol());
    }
}
