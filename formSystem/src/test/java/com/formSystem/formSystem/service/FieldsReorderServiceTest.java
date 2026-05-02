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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FieldsReorderServiceTest {
    @Mock
    private FieldsRepository fieldsRepository;

    @Mock
    private FormsRepository formsRepository;

    @InjectMocks
    private FieldsService fieldsService;

    private Form form;
    private Field field1, field2, field3;

    @BeforeEach
    void setUp() {
        form = new Form();
        form.setId(1L);
        form.setTitle("Test Form");

        field1 = new Field();
        field1.setId(1L);
        field1.setLabel("Field 1");
        field1.setType("text");
        field1.setFieldOrder(0);
        field1.setForm(form);

        field2 = new Field();
        field2.setId(2L);
        field2.setLabel("Field 2");
        field2.setType("text");
        field2.setFieldOrder(1);
        field2.setForm(form);

        field3 = new Field();
        field3.setId(3L);
        field3.setLabel("Field 3");
        field3.setType("text");
        field3.setFieldOrder(2);
        field3.setForm(form);
    }

    @Test
    void reorderFields_ShouldThrowException_WhenFormNotFound() {
        when(formsRepository.findById(1L)).thenReturn(Optional.empty());
        List<Long> fieldIds = Arrays.asList(1L, 2L, 3L);

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, 
            () -> fieldsService.reorderFields(1L, fieldIds));
        assertTrue(ex.getMessage().contains("Form not found"));
    }

    @Test
    void reorderFields_ShouldThrowException_WhenFieldNotFound() {
        when(formsRepository.findById(1L)).thenReturn(Optional.of(form));
        when(fieldsRepository.findById(1L)).thenReturn(Optional.of(field1));
        when(fieldsRepository.findById(99L)).thenReturn(Optional.empty());
        List<Long> fieldIds = Arrays.asList(1L, 99L);

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, 
            () -> fieldsService.reorderFields(1L, fieldIds));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    void reorderFields_ShouldThrowException_WhenFieldNotBelongToForm() {
        Form otherForm = new Form();
        otherForm.setId(2L);
        field3.setForm(otherForm);

        when(formsRepository.findById(1L)).thenReturn(Optional.of(form));
        when(fieldsRepository.findById(1L)).thenReturn(Optional.of(field1));
        when(fieldsRepository.findById(2L)).thenReturn(Optional.of(field2));
        when(fieldsRepository.findById(3L)).thenReturn(Optional.of(field3));

        List<Long> fieldIds = Arrays.asList(1L, 2L, 3L);

        ValidationException ex = assertThrows(ValidationException.class, 
            () -> fieldsService.reorderFields(1L, fieldIds));
        assertTrue(ex.getMessage().contains("does not belong to this form"));
    }

    @Test
    void reorderFields_ShouldUpdateOrder_WhenReverseOrder() {
        when(formsRepository.findById(1L)).thenReturn(Optional.of(form));
        when(fieldsRepository.findById(3L)).thenReturn(Optional.of(field3));
        when(fieldsRepository.findById(2L)).thenReturn(Optional.of(field2));
        when(fieldsRepository.findById(1L)).thenReturn(Optional.of(field1));
        when(fieldsRepository.save(any(Field.class))).thenAnswer(i -> i.getArgument(0));

        List<Long> fieldIds = Arrays.asList(3L, 2L, 1L);
        fieldsService.reorderFields(1L, fieldIds);

        // Verify save was called 3 times with correct order
        verify(fieldsRepository, times(3)).save(any(Field.class));
        
        // Verify field order was updated
        assertEquals(0, field3.getFieldOrder());
        assertEquals(1, field2.getFieldOrder());
        assertEquals(2, field1.getFieldOrder());
    }

    @Test
    void reorderFields_ShouldUpdateOrder_WhenRandomOrder() {
        when(formsRepository.findById(1L)).thenReturn(Optional.of(form));
        when(fieldsRepository.findById(2L)).thenReturn(Optional.of(field2));
        when(fieldsRepository.findById(1L)).thenReturn(Optional.of(field1));
        when(fieldsRepository.findById(3L)).thenReturn(Optional.of(field3));
        when(fieldsRepository.save(any(Field.class))).thenAnswer(i -> i.getArgument(0));

        List<Long> fieldIds = Arrays.asList(2L, 1L, 3L);
        fieldsService.reorderFields(1L, fieldIds);

        verify(fieldsRepository, times(3)).save(any(Field.class));
        
        assertEquals(0, field2.getFieldOrder());
        assertEquals(1, field1.getFieldOrder());
        assertEquals(2, field3.getFieldOrder());
    }

    @Test
    void reorderFields_ShouldWork_WithOnlyOneField() {
        when(formsRepository.findById(1L)).thenReturn(Optional.of(form));
        when(fieldsRepository.findById(1L)).thenReturn(Optional.of(field1));
        when(fieldsRepository.save(any(Field.class))).thenAnswer(i -> i.getArgument(0));

        List<Long> fieldIds = Arrays.asList(1L);
        fieldsService.reorderFields(1L, fieldIds);

        verify(fieldsRepository, times(1)).save(field1);
        assertEquals(0, field1.getFieldOrder());
    }

    @Test
    void reorderFields_ShouldWork_WithTwoFields() {
        when(formsRepository.findById(1L)).thenReturn(Optional.of(form));
        when(fieldsRepository.findById(1L)).thenReturn(Optional.of(field1));
        when(fieldsRepository.findById(2L)).thenReturn(Optional.of(field2));
        when(fieldsRepository.save(any(Field.class))).thenAnswer(i -> i.getArgument(0));

        List<Long> fieldIds = Arrays.asList(2L, 1L);
        fieldsService.reorderFields(1L, fieldIds);

        verify(fieldsRepository, times(2)).save(any(Field.class));
        assertEquals(0, field2.getFieldOrder());
        assertEquals(1, field1.getFieldOrder());
    }

    @Test
    void reorderFields_ShouldAccept_EmptyList() {
        when(formsRepository.findById(1L)).thenReturn(Optional.of(form));

        List<Long> fieldIds = new ArrayList<>();
        assertDoesNotThrow(() -> fieldsService.reorderFields(1L, fieldIds));
        
        verify(fieldsRepository, never()).save(any());
    }
}
