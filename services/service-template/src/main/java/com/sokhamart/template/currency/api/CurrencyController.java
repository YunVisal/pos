package com.sokhamart.template.currency.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sokhamart.template.currency.application.CurrencyService;
import com.sokhamart.template.currency.domain.Currency;

@RestController
@RequestMapping("/api/v1/currencies")
public class CurrencyController {
    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CurrencyResponse create(@RequestBody CreateCurrencyRequest currency) {
        Currency newCurrency = currencyService.create(currency.code(), currency.name(), currency.symbol());
        return CurrencyResponse.from(newCurrency);
    }

    @GetMapping
    List<CurrencyResponse> getAll() {
        return currencyService.findAll().stream().map(CurrencyResponse::from).toList();
    }
}
