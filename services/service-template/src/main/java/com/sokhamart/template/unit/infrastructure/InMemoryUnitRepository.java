package com.sokhamart.template.unit.infrastructure;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.sokhamart.template.unit.domain.Unit;
import com.sokhamart.template.unit.domain.UnitRepository;

@Repository
class InMemoryUnitRepository implements UnitRepository {
    private final Map<String, Unit> units = new ConcurrentHashMap<>();

    @Override
    public Unit save(Unit unit) {
        units.put(unit.code(), unit);
        return unit;
    }

    @Override
    public Optional<Unit> findByCode(String code) {
        return Optional.ofNullable(units.get(code));
    }

    @Override
    public List<Unit> findAll() {
        return List.copyOf(units.values());
    }

}
