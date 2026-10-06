package com.sokhamart.template.unit.application;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.sokhamart.template.unit.domain.DuplicateUnitCodeException;
import com.sokhamart.template.unit.domain.Unit;
import com.sokhamart.template.unit.domain.UnitRepository;

class UnitServiceTest {
    @Test
    void testReturnCreatedUnit() {
        UnitRepository repository = new FakeUnitRepository();
        UnitService service = new UnitService(repository);
        service.create("KG", "Kilogram");
        List<Unit> units = service.findAll();
        boolean contained = units.stream().anyMatch(obj -> "KG".equals(obj.code()));
        assertTrue(contained, "findAll() return the list that contained the created unit");
    }

    @Test
    void testThrowDuplicateUnitCodeExceptionWhenTheSavedCodeIsAlreadyExist() {
        UnitRepository repository = new FakeUnitRepository();
        UnitService service = new UnitService(repository);
        service.create("KG", "Kilogram");
        assertThrows(DuplicateUnitCodeException.class, () -> {
            service.create("KG", "Kilogram");
        });
    }
}
