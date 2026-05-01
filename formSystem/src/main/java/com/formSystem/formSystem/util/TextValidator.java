package com.formSystem.formSystem.util;

import com.formSystem.formSystem.exception.ValidationException;
import com.formSystem.formSystem.model.Field;
import org.springframework.stereotype.Component;

@Component
public class TextValidator implements FieldValidator {
    @Override
    public boolean supports(String type) { return "text".equalsIgnoreCase(type); }

    @Override
    public void validate(String value, Field config) {
        if (config.getRequired() && (value == null || value.isBlank())) {
            throw new ValidationException(config.getLabel() + " trường này không được để trống");
        }
        if (value != null && !value.isBlank()) {
            // Check min length
            if (config.getMinValue() != null && value.length() < config.getMinValue()) {
                throw new ValidationException(config.getLabel() + " phải >= " + config.getMinValue() + " ký tự");
            }
            // Check max length
            if (config.getMaxValue() != null && value.length() > config.getMaxValue()) {
                throw new ValidationException(config.getLabel() + " không thể quá " + config.getMaxValue() + " ký tự");
            }
            // Default max length if no config
            if (config.getMaxValue() == null && value.length() > 200) {
                throw new ValidationException(config.getLabel() + " không thể quá 200 ký tự");
            }
        }
    }
}