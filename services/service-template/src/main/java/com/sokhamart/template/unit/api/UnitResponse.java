package com.sokhamart.template.unit.api;

import com.sokhamart.template.unit.domain.Unit;

public record UnitResponse(String code, String name) {
    static UnitResponse from(Unit unit) {
        return new UnitResponse(unit.code(), unit.name());
    }
}
