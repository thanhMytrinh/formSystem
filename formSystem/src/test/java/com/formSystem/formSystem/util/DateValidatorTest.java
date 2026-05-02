package com.formSystem.formSystem.util;

import com.formSystem.formSystem.exception.ValidationException;
import com.formSystem.formSystem.model.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateValidatorTest {
    private DateValidator validator;
    private Field field;

    @BeforeEach
    void setUp() {
        validator = new DateValidator();
        field = new Field();
        field.setLabel("Meeting Date");
        field.setType("date");
    }

    @Test
    void supports_ShouldReturnTrue_ForDateType() {
        assertTrue(validator.supports("date"));
        assertTrue(validator.supports("DATE"));
        assertTrue(validator.supports("Date"));
    }

    @Test
    void supports_ShouldReturnFalse_ForOtherTypes() {
        assertFalse(validator.supports("text"));
        assertFalse(validator.supports("number"));
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
    void validate_ShouldThrowException_WhenFormatIsInvalid() {
        field.setRequired(false);
        
        ValidationException ex = assertThrows(ValidationException.class, 
            () -> validator.validate("2025/01/01", field));
        assertTrue(ex.getMessage().contains("phải đúng định dạng"));
    }

    @Test
    void validate_ShouldThrowException_WhenFormatIsInvalid2() {
        field.setRequired(false);
        
        ValidationException ex = assertThrows(ValidationException.class, 
            () -> validator.validate("01-01-2025", field));
        assertTrue(ex.getMessage().contains("phải đúng định dạng"));
    }

    @Test
    void validate_ShouldThrowException_WhenFormatIsInvalid3() {
        field.setRequired(false);
        
        ValidationException ex = assertThrows(ValidationException.class, 
            () -> validator.validate("2025-13-01", field));
        assertTrue(ex.getMessage().contains("phải đúng định dạng"));
    }

    @Test
    void validate_ShouldThrowException_WhenDateIsInPast() {
        field.setRequired(false);
        LocalDate pastDate = LocalDate.now().minusDays(1);
        
        ValidationException ex = assertThrows(ValidationException.class, 
            () -> validator.validate(pastDate.toString(), field));
        assertTrue(ex.getMessage().contains("không thể chọn ngày trong quá khứ"));
    }

    @Test
    void validate_ShouldPass_WhenDateIsToday() {
        field.setRequired(false);
        String today = LocalDate.now().toString();
        
        assertDoesNotThrow(() -> validator.validate(today, field));
    }

    @Test
    void validate_ShouldPass_WhenDateIsInFuture() {
        field.setRequired(false);
        LocalDate futureDate = LocalDate.now().plusDays(10);
        
        assertDoesNotThrow(() -> validator.validate(futureDate.toString(), field));
    }

    @Test
    void validate_ShouldPass_WhenDateIsCorrectFormatAndFuture() {
        field.setRequired(false);
        String futureDate = LocalDate.now().plusDays(30).toString();
        
        assertDoesNotThrow(() -> validator.validate(futureDate, field));
    }

    @Test
    void validate_ShouldThrowException_WhenDateIsInvalidDay() {
        field.setRequired(false);
        
        ValidationException ex = assertThrows(ValidationException.class, 
            () -> validator.validate("2025-02-30", field));
        assertTrue(ex.getMessage().contains("phải đúng định dạng"));
    }
}
