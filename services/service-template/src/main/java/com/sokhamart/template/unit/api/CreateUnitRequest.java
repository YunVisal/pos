package com.sokhamart.template.unit.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUnitRequest(
        @Size(max = 10, message = "must not longer than 10 characters") @Pattern(regexp = "^[A-Z0-9]+$", message = "must be uppercase") String code,

        @NotBlank String name) {

}
