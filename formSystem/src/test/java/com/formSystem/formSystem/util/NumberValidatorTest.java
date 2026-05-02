package com.formSystem.formSystem.util;

import com.formSystem.formSystem.exception.ValidationException;
import com.formSystem.formSystem.model.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumberValidatorTest {
    private NumberValidator validator;
    private Field field;

    @BeforeEach
    void setUp() {
        validator = new NumberValidator();
        field = new Field();
        field.setLabel("Age");
        field.setType("number");
    }

    @Test
    void supports_ShouldReturnTrue_ForNumberType() {
        assertTrue(validator.supports("number"));
        assertTrue(validator.supports("NUMBER"));
        assertTrue(validator.supports("Number"));
    }

    @Test
    void supports_ShouldReturnFalse_ForOtherTypes() {
        assertFalse(validator.supports("text"));
        assertFalse(validator.supports("date"));
        assertFalse(validator.supports("color"));
    }

    @Test
    void validate_ShouldPass_WhenValueIsNull() {
        field.setRequired(false);
        assertDoesNotThrow(() -> validator.validate(null, field));
    }

    @Test
    void validate_ShouldPass_WhenValueIsBlank() {
        field.setRequired(false);
        assertDoesNotThrow(() -> validator.validate("   ", field));
    }

    @Test
    void validate_ShouldThrowException_WhenValueIsNotNumber() {
        field.setRequired(false);
        
        ValidationException ex = assertThrows(ValidationException.class, 
            () -> validator.validate("abc", field));
        assertTrue(ex.getMessage().contains("phải là 1 số"));
    }

    @Test
    void validate_ShouldThrowException_WhenValueIsDecimal() {
        field.setRequired(false);
        
        ValidationException ex = assertThrows(ValidationException.class, 
            () -> validator.validate("10.5", field));
        assertTrue(ex.getMessage().contains("phải là 1 số"));
    }

    @Test
    void validate_ShouldThrowException_WhenValueBelowMinValue() {
        field.setRequired(false);
        field.setMinValue(18);
        
        ValidationException ex = assertThrows(ValidationException.class, 
            () -> validator.validate("15", field));
        assertTrue(ex.getMessage().contains("phải >= 18"));
    }

    @Test
    void validate_ShouldPass_WhenValueEqualsMinValue() {
        field.setRequired(false);
        field.setMinValue(18);
        
        assertDoesNotThrow(() -> validator.validate("18", field));
    }

    @Test
    void validate_ShouldThrowException_WhenValueAboveMaxValue() {
        field.setRequired(false);
        field.setMaxValue(100);
        
        ValidationException ex = assertThrows(ValidationException.class, 
            () -> validator.validate("105", field));
        assertTrue(ex.getMessage().contains("phải <=100"));
    }

    @Test
    void validate_ShouldPass_WhenValueEqualsMaxValue() {
        field.setRequired(false);
        field.setMaxValue(100);
        
        assertDoesNotThrow(() -> validator.validate("100", field));
    }

    @Test
    void validate_ShouldPass_WhenValueBetweenMinAndMax() {
        field.setRequired(false);
        field.setMinValue(18);
        field.setMaxValue(100);
        
        assertDoesNotThrow(() -> validator.validate("50", field));
    }

    @Test
    void validate_ShouldPass_WhenNoMinMaxAndValueIsValid() {
        field.setRequired(false);
        field.setMinValue(null);
        field.setMaxValue(null);
        
        assertDoesNotThrow(() -> validator.validate("12345", field));
    }

    @Test
    void validate_ShouldPass_WhenValueIsNegativeNumber() {
        field.setRequired(false);
        
        assertDoesNotThrow(() -> validator.validate("-50", field));
    }

    @Test
    void validate_ShouldPass_WhenValueIsZero() {
        field.setRequired(false);
        
        assertDoesNotThrow(() -> validator.validate("0", field));
    }
}
