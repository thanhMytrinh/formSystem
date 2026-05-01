package com.formSystem.formSystem.util;

import com.formSystem.formSystem.model.Field;

public interface FieldValidator {
    boolean supports(String type);

    void validate(String value, Field config);
}