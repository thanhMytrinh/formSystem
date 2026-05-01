package com.formSystem.formSystem.util;

import com.formSystem.formSystem.exception.ValidationException;
import com.formSystem.formSystem.model.Field;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Component
public class DateValidator implements FieldValidator {
    @Override
    public boolean supports(String type) { return "date".equalsIgnoreCase(type); }

    @Override
    public void validate(String value, Field config) {
        if (value == null || value.isBlank()) return;
        try {
            LocalDate date = LocalDate.parse(value);
            if (date.isBefore(LocalDate.now())) {
                throw new ValidationException(config.getLabel() + " không thể chọn ngày trong quá khứ");
            }
        } catch (DateTimeParseException e) {
            throw new ValidationException(config.getLabel() + " phải đúng định dạng  (yyyy-MM-dd)");
        }
    }
}