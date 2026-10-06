package com.sokhamart.template.unit.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sokhamart.template.unit.application.UnitService;
import com.sokhamart.template.unit.domain.Unit;

@RestController
@RequestMapping("/api/v1/units")
public class UnitController {
    private final UnitService service;

    public UnitController(UnitService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UnitResponse create(@RequestBody CreateUnitRequest unit) {
        Unit newUnit = service.create(unit.code(), unit.name());
        return UnitResponse.from(newUnit);
    }

    @GetMapping
    List<UnitResponse> getAll() {
        return service.findAll().stream().map(UnitResponse::from).toList();
    }
}
