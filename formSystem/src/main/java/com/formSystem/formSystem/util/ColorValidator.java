package com.formSystem.formSystem.util;

import com.formSystem.formSystem.exception.ValidationException;
import com.formSystem.formSystem.model.Field;
import org.springframework.stereotype.Component;

@Component
public class ColorValidator implements FieldValidator {
    private static final String HEX_PATTERN = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";

    @Override
    public boolean supports(String type) { return "color".equalsIgnoreCase(type); }

    @Override
    public void validate(String value, Field config) {
        if (value == null || value.isBlank()) return;
        if (!value.matches(HEX_PATTERN)) {
            throw new ValidationException(config.getLabel() + " phải là HEX code hợp lệ (e.g., #FFFFFF)");
        }
    }
}