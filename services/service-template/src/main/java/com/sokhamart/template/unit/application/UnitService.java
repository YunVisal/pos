package com.sokhamart.template.unit.application;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sokhamart.template.unit.domain.DuplicateUnitCodeException;
import com.sokhamart.template.unit.domain.Unit;
import com.sokhamart.template.unit.domain.UnitRepository;

@Service
public class UnitService {
    private final UnitRepository repository;

    public UnitService(UnitRepository repository) {
        this.repository = repository;
    }

    public Unit create(String code, String name) {
        Optional<Unit> existingUnit = repository.findByCode(code);
        if (existingUnit.isPresent()) {
            throw new DuplicateUnitCodeException("Unit code already exists: " + code);
        }
        return repository.save(new Unit(code, name));
    }

    public List<Unit> findAll() {
        return repository.findAll();
    }
}
