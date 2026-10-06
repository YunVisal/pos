package com.sokhamart.template.unit.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    ResponseEntity<Unit> create(@RequestBody UnitRequest unit) {
        return new ResponseEntity<>(service.create(unit.code(), unit.name()), HttpStatus.CREATED);
    }

    @GetMapping
    ResponseEntity<List<Unit>> getAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }
}
