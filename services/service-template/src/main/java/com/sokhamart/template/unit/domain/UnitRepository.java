package com.sokhamart.template.unit.domain;

import java.util.List;
import java.util.Optional;

public interface UnitRepository {
    Unit save(Unit unit);

    Optional<Unit> findByCode(String code);

    List<Unit> findAll();
}
