package com.formSystem.formSystem.util;

import com.formSystem.formSystem.exception.ValidationException;
import com.formSystem.formSystem.model.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorValidatorTest {
    private ColorValidator validator;
    private Field field;

    @BeforeEach
    void setUp() {
        validator = new ColorValidator();
        field = new Field();
        field.setLabel("Brand Color");
        field.setType("color");
    }

    @Test
    void supports_ShouldReturnTrue_ForColorType() {
        assertTrue(validator.supports("color"));
        assertTrue(validator.supports("COLOR"));
        assertTrue(validator.supports("Color"));
    }

    @Test
    void supports_ShouldReturnFalse_ForOtherTypes() {
        assertFalse(validator.supports("text"));
        assertFalse(validator.supports("number"));
        assertFalse(validator.supports("date"));
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
    void validate_ShouldPass_With6DigitHexCode() {
        field.setRequired(false);
        assertDoesNotThrow(() -> validator.validate("#FFFFFF", field));
    }

    @Test
    void validate_ShouldPass_With6DigitHexCodeLowercase() {
        field.setRequired(false);
        assertDoesNotThrow(() -> validator.validate("#ffffff", field));
    }

    @Test
    void validate_ShouldPass_With3DigitHexCode() {
        field.setRequired(false);
        assertDoesNotThrow(() -> validator.validate("#FFF", field));
    }

    @Test
    void validate_ShouldPass_With3DigitHexCodeLowercase() {
        field.setRequired(false);
        assertDoesNotThrow(() -> validator.validate("#fff", field));
    }

    @Test
    void validate_ShouldPass_WithMixedCaseHexCode() {
        field.setRequired(false);
        assertDoesNotThrow(() -> validator.validate("#FfFfFf", field));
    }

    @Test
    void validate_ShouldThrowException_WhenMissingHashSymbol() {
        field.setRequired(false);
        
        ValidationException ex = assertThrows(ValidationException.class, 
            () -> validator.validate("FFFFFF", field));
        assertTrue(ex.getMessage().contains("phải là HEX code hợp lệ"));
    }

    @Test
    void validate_ShouldThrowException_WhenInvalidHexDigits() {
        field.setRequired(false);
        
        ValidationException ex = assertThrows(ValidationException.class, 
            () -> validator.validate("#GGGGGG", field));
        assertTrue(ex.getMessage().contains("phải là HEX code hợp lệ"));
    }

    @Test
    void validate_ShouldThrowException_WhenTooManyDigits() {
        field.setRequired(false);
        
        ValidationException ex = assertThrows(ValidationException.class, 
            () -> validator.validate("#FFFFFFF", field));
        assertTrue(ex.getMessage().contains("phải là HEX code hợp lệ"));
    }

    @Test
    void validate_ShouldThrowException_WhenTooFewDigits() {
        field.setRequired(false);
        
        ValidationException ex = assertThrows(ValidationException.class, 
            () -> validator.validate("#FF", field));
        assertTrue(ex.getMessage().contains("phải là HEX code hợp lệ"));
    }

    @Test
    void validate_ShouldThrowException_WhenInvalidFormat() {
        field.setRequired(false);
        
        ValidationException ex = assertThrows(ValidationException.class, 
            () -> validator.validate("rgb(255,255,255)", field));
        assertTrue(ex.getMessage().contains("phải là HEX code hợp lệ"));
    }

    @Test
    void validate_ShouldPass_WithValidBlackColor() {
        field.setRequired(false);
        assertDoesNotThrow(() -> validator.validate("#000000", field));
    }

    @Test
    void validate_ShouldPass_WithValidRedColor() {
        field.setRequired(false);
        assertDoesNotThrow(() -> validator.validate("#FF0000", field));
    }

    @Test
    void validate_ShouldPass_WithValidGreenColor() {
        field.setRequired(false);
        assertDoesNotThrow(() -> validator.validate("#00FF00", field));
    }

    @Test
    void validate_ShouldPass_WithValidBlueColor() {
        field.setRequired(false);
        assertDoesNotThrow(() -> validator.validate("#0000FF", field));
    }

    @Test
    void validate_ShouldThrowException_WhenEmptyAfterHash() {
        field.setRequired(false);
        
        ValidationException ex = assertThrows(ValidationException.class, 
            () -> validator.validate("#", field));
        assertTrue(ex.getMessage().contains("phải là HEX code hợp lệ"));
    }
}
