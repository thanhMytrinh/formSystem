package com.formSystem.formSystem.util;

import com.formSystem.formSystem.exception.ValidationException;
import com.formSystem.formSystem.model.Field;
import org.springframework.stereotype.Component;

@Component
public class NumberValidator implements FieldValidator {
    @Override
    public boolean supports(String type) { return "number".equalsIgnoreCase(type); }

    @Override
    public void validate(String value, Field config) {
        if (value == null || value.isBlank()) return;
        try {
            int val = Integer.parseInt(value);
            if (config.getMinValue() != null && val < config.getMinValue())
                throw new ValidationException(config.getLabel() + " phải >= " + config.getMinValue());
            if (config.getMaxValue() != null && val > config.getMaxValue())
                throw new ValidationException(config.getLabel() + " phải <=" + config.getMaxValue());
        } catch (NumberFormatException e) {
            throw new ValidationException(config.getLabel() + " phải là 1 số");
        }
    }
}