package com.showroom.common;

import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class JsonValidator {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static String validate(Object object) {
        Set<ConstraintViolation<Object>> violations = validator.validate(object);
        if (violations.isEmpty()) {
            return null;
        }
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
    }
}
