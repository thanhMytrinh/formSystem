package com.formSystem.formSystem.util;

import com.formSystem.formSystem.exception.ValidationException;
import com.formSystem.formSystem.model.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextValidatorTest {
    private TextValidator validator;
    private Field field;

    @BeforeEach
    void setUp() {
        validator = new TextValidator();
        field = new Field();
        field.setLabel("Full Name");
        field.setType("text");
    }

    @Test
    void supports_ShouldReturnTrue_ForTextType() {
        assertTrue(validator.supports("text"));
        assertTrue(validator.supports("TEXT"));
        assertTrue(validator.supports("Text"));
    }

    @Test
    void supports_ShouldReturnFalse_ForOtherTypes() {
        assertFalse(validator.supports("number"));
        assertFalse(validator.supports("date"));
        assertFalse(validator.supports("color"));
    }

    @Test
    void validate_ShouldThrowException_WhenRequiredFieldIsNull() {
        field.setRequired(true);
        
        ValidationException ex = assertThrows(ValidationException.class, 
            () -> validator.validate(null, field));
        assertTrue(ex.getMessage().contains("không được để trống"));
    }

    @Test
    void validate_ShouldThrowException_WhenRequiredFieldIsBlank() {
        field.setRequired(true);
        
        ValidationException ex = assertThrows(ValidationException.class, 
            () -> validator.validate("   ", field));
        assertTrue(ex.getMessage().contains("không được để trống"));
    }

    @Test
    void validate_ShouldPass_WhenNotRequiredAndNull() {
        field.setRequired(false);
        assertDoesNotThrow(() -> validator.validate(null, field));
    }

    @Test
    void validate_ShouldPass_WhenNotRequiredAndBlank() {
        field.setRequired(false);
        assertDoesNotThrow(() -> validator.validate("   ", field));
    }

    @Test
    void validate_ShouldThrowException_WhenLengthExceedsMaxValue() {
        field.setRequired(false);
        field.setMaxValue(10);
        String value = "This is a very long text";
        
        ValidationException ex = assertThrows(ValidationException.class, 
            () -> validator.validate(value, field));
        assertTrue(ex.getMessage().contains("không thể quá 10 ký tự"));
    }

    @Test
    void validate_ShouldPass_WhenLengthEqualsMaxValue() {
        field.setRequired(false);
        field.setMaxValue(10);
        String value = "1234567890";
        
        assertDoesNotThrow(() -> validator.validate(value, field));
    }

    @Test
    void validate_ShouldThrowException_WhenLengthBelowMinValue() {
        field.setRequired(false);
        field.setMinValue(5);
        String value = "Hi";
        
        ValidationException ex = assertThrows(ValidationException.class, 
            () -> validator.validate(value, field));
        assertTrue(ex.getMessage().contains("phải >= 5 ký tự"));
    }

    @Test
    void validate_ShouldPass_WhenLengthEqualsMinValue() {
        field.setRequired(false);
        field.setMinValue(5);
        String value = "Hello";
        
        assertDoesNotThrow(() -> validator.validate(value, field));
    }

    @Test
    void validate_ShouldPass_WhenLengthBetweenMinAndMax() {
        field.setRequired(false);
        field.setMinValue(5);
        field.setMaxValue(20);
        String value = "Hello World";
        
        assertDoesNotThrow(() -> validator.validate(value, field));
    }

    @Test
    void validate_ShouldThrowException_WhenLengthExceedsDefault200() {
        field.setRequired(false);
        field.setMaxValue(null);
        String value = "a".repeat(201);
        
        ValidationException ex = assertThrows(ValidationException.class, 
            () -> validator.validate(value, field));
        assertTrue(ex.getMessage().contains("không thể quá 200 ký tự"));
    }

    @Test
    void validate_ShouldPass_WhenLengthWith200Limit() {
        field.setRequired(false);
        field.setMaxValue(null);
        String value = "a".repeat(200);
        
        assertDoesNotThrow(() -> validator.validate(value, field));
    }

    @Test
    void validate_ShouldPass_WhenValueIsValid() {
        field.setRequired(true);
        field.setMinValue(3);
        field.setMaxValue(50);
        String value = "John Doe";
        
        assertDoesNotThrow(() -> validator.validate(value, field));
    }
}
