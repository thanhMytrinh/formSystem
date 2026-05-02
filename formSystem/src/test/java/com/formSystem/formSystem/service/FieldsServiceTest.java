package com.formSystem.formSystem.service;

import com.formSystem.formSystem.exception.ValidationException;
import com.formSystem.formSystem.model.Field;
import com.formSystem.formSystem.model.Form;
import com.formSystem.formSystem.repository.FieldsRepository;
import com.formSystem.formSystem.repository.FormsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FieldsServiceTest {
    @Mock
    private FieldsRepository fieldsRepository;

    @Mock
    private FormsRepository formsRepository;

    @InjectMocks
    private FieldsService fieldsService;

    private Form form;
    private Field field;

    @BeforeEach
    void setUp() {
        form = new Form();
        form.setId(1L);
        form.setTitle("Contact Form");

        field = new Field();
        field.setId(1L);
        field.setLabel("Full Name");
        field.setType("text");
        field.setRequired(true);
    }

    @Test
    void addFieldToForm_ShouldThrowException_WhenFormNotFound() {
        when(formsRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, 
            () -> fieldsService.addFieldToForm(1L, field));
        verify(formsRepository, times(1)).findById(1L);
        verify(fieldsRepository, never()).save(any());
    }

    @Test
    void addFieldToForm_ShouldThrowException_WhenLabelIsEmpty() {
        field.setLabel("");
        when(formsRepository.findById(1L)).thenReturn(Optional.of(form));

        ValidationException ex = assertThrows(ValidationException.class, 
            () -> fieldsService.addFieldToForm(1L, field));
        assertTrue(ex.getMessage().contains("Label không được để trống"));
    }

    @Test
    void addFieldToForm_ShouldThrowException_WhenTypeIsEmpty() {
        field.setType("");
        when(formsRepository.findById(1L)).thenReturn(Optional.of(form));

        ValidationException ex = assertThrows(ValidationException.class, 
            () -> fieldsService.addFieldToForm(1L, field));
        assertTrue(ex.getMessage().contains("Type không được để trống"));
    }

    @Test
    void addFieldToForm_ShouldThrowException_WhenTypeIsInvalid() {
        field.setType("invalid_type");
        when(formsRepository.findById(1L)).thenReturn(Optional.of(form));

        ValidationException ex = assertThrows(ValidationException.class, 
            () -> fieldsService.addFieldToForm(1L, field));
        assertTrue(ex.getMessage().contains("Type phải là một trong"));
    }

    @Test
    void addFieldToForm_ShouldThrowException_WhenSelectHasNoOptions() {
        field.setType("select");
        field.setOptions("");
        when(formsRepository.findById(1L)).thenReturn(Optional.of(form));

        ValidationException ex = assertThrows(ValidationException.class, 
            () -> fieldsService.addFieldToForm(1L, field));
        assertTrue(ex.getMessage().contains("Field select phải có options"));
    }

    @Test
    void addFieldToForm_ShouldThrowException_WhenSelectOptionsInvalidFormat() {
        field.setType("select");
        field.setOptions("A, B, C");
        when(formsRepository.findById(1L)).thenReturn(Optional.of(form));

        ValidationException ex = assertThrows(ValidationException.class, 
            () -> fieldsService.addFieldToForm(1L, field));
        assertTrue(ex.getMessage().contains("Options phải là JSON array format"));
    }

    @Test
    void addFieldToForm_ShouldThrowException_WhenMinGreaterThanMax() {
        field.setMinValue(100);
        field.setMaxValue(50);
        when(formsRepository.findById(1L)).thenReturn(Optional.of(form));

        ValidationException ex = assertThrows(ValidationException.class, 
            () -> fieldsService.addFieldToForm(1L, field));
        assertTrue(ex.getMessage().contains("minValue không thể lớn hơn maxValue"));
    }

    @Test
    void addFieldToForm_ShouldSucceed_WithValidTextField() {
        when(formsRepository.findById(1L)).thenReturn(Optional.of(form));
        when(fieldsRepository.save(any(Field.class))).thenReturn(field);

        Field result = fieldsService.addFieldToForm(1L, field);

        assertNotNull(result);
        assertEquals("Full Name", result.getLabel());
        verify(formsRepository, times(1)).findById(1L);
        verify(fieldsRepository, times(1)).save(field);
    }

    @Test
    void addFieldToForm_ShouldSucceed_WithValidSelectField() {
        field.setType("select");
        field.setOptions("[\"Option A\", \"Option B\"]");
        when(formsRepository.findById(1L)).thenReturn(Optional.of(form));
        when(fieldsRepository.save(any(Field.class))).thenReturn(field);

        Field result = fieldsService.addFieldToForm(1L, field);

        assertNotNull(result);
        verify(fieldsRepository, times(1)).save(field);
    }

    @Test
    void addFieldToForm_ShouldSucceed_WithValidNumberField() {
        field.setType("number");
        field.setMinValue(0);
        field.setMaxValue(100);
        when(formsRepository.findById(1L)).thenReturn(Optional.of(form));
        when(fieldsRepository.save(any(Field.class))).thenReturn(field);

        Field result = fieldsService.addFieldToForm(1L, field);

        assertNotNull(result);
        verify(fieldsRepository, times(1)).save(field);
    }

    @Test
    void updateField_ShouldThrowException_WhenFieldNotFound() {
        when(fieldsRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, 
            () -> fieldsService.updateField(1L, field));
        verify(fieldsRepository, never()).save(any());
    }

    @Test
    void updateField_ShouldThrowException_WhenNewTypeIsInvalid() {
        field.setType("invalid");
        when(fieldsRepository.findById(1L)).thenReturn(Optional.of(new Field()));

        ValidationException ex = assertThrows(ValidationException.class, 
            () -> fieldsService.updateField(1L, field));
        assertTrue(ex.getMessage().contains("Type phải là một trong"));
    }

    @Test
    void updateField_ShouldSucceed_WithValidChanges() {
        Field existingField = new Field();
        existingField.setId(1L);
        existingField.setLabel("Old Label");
        existingField.setType("text");

        when(fieldsRepository.findById(1L)).thenReturn(Optional.of(existingField));
        when(fieldsRepository.save(any(Field.class))).thenReturn(existingField);

        field.setLabel("New Label");
        Field result = fieldsService.updateField(1L, field);

        assertNotNull(result);
        verify(fieldsRepository, times(1)).save(existingField);
    }

    @Test
    void deleteField_ShouldDeleteSuccessfully() {
        fieldsService.deleteField(1L);
        verify(fieldsRepository, times(1)).deleteById(1L);
    }

    @Test
    void addFieldToForm_ShouldPass_AllValidTypes() {
        when(formsRepository.findById(1L)).thenReturn(Optional.of(form));
        when(fieldsRepository.save(any(Field.class))).thenReturn(field);

        String[] validTypes = {"text", "number", "date", "color", "select"};
        for (String type : validTypes) {
            field.setType(type);
            if ("select".equalsIgnoreCase(type)) {
                field.setOptions("[\"A\", \"B\"]");
            }
            
            assertDoesNotThrow(() -> fieldsService.addFieldToForm(1L, field));
        }
    }
}
