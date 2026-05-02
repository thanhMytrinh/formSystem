package com.formSystem.formSystem.service;

import com.formSystem.formSystem.exception.FormValidationException;
import com.formSystem.formSystem.model.Field;
import com.formSystem.formSystem.model.Form;
import com.formSystem.formSystem.model.Submission;
import com.formSystem.formSystem.repository.FormsRepository;
import com.formSystem.formSystem.repository.SubmissionsRepository;
import com.formSystem.formSystem.service.SubmissionsService;
import com.formSystem.formSystem.util.FieldValidator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubmissionsServiceTest {

    @Mock
    private SubmissionsRepository submissionRepository;

    @Mock
    private FormsRepository formRepository;

    @Spy
    private List<FieldValidator> validators = new ArrayList<>();

    @Mock
    private FieldValidator mockValidator;

    @InjectMocks
    private SubmissionsService submissionsService;

    private Form mockForm;
    private Field mockField;

    @BeforeEach
    void setUp() {
        mockField = new Field();
        mockField.setId(1L);
        mockField.setLabel("Họ tên");
        mockField.setType("text");
        mockField.setRequired(true);

        mockForm = new Form();
        mockForm.setId(10L);
        mockForm.setFields(new ArrayList<>(List.of(mockField)));
    }

    @Test
    @DisplayName("Ném lỗi EntityNotFound khi Form không tồn tại")
    void submitForm_ShouldThrowEntityNotFound_WhenFormNotExists() {
        when(formRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                submissionsService.submitForm(99L, "My", new HashMap<>())
        );
    }

    @Test
    @DisplayName("Ném lỗi khi Form không có bất kỳ field nào")
    void submitForm_ShouldThrowException_WhenFormHasNoFields() {
        mockForm.setFields(new ArrayList<>());
        when(formRepository.findById(10L)).thenReturn(Optional.of(mockForm));

        FormValidationException ex = assertThrows(FormValidationException.class, () ->
                submissionsService.submitForm(10L, "My", new HashMap<>())
        );
        assertEquals("Hệ thống lỗi: Form này chưa được cấu hình câu hỏi.", ex.getMessage());
    }

    @Test
    @DisplayName("Ném lỗi khi trường bắt buộc bị thiếu trong answers")
    void submitForm_ShouldThrowException_WhenRequiredFieldIsMissing() {
        when(formRepository.findById(10L)).thenReturn(Optional.of(mockForm));

        Map<Long, String> emptyAnswers = new HashMap<>();

        FormValidationException ex = assertThrows(FormValidationException.class, () ->
                submissionsService.submitForm(10L, "My", emptyAnswers)
        );
        assertTrue(ex.getMessage().contains("là bắt buộc"));
    }

    @Test
    @DisplayName("Gọi validator khi có dữ liệu hợp lệ")
    void submitForm_ShouldInvokeValidator_WhenValueIsPresent() {
        validators.add(mockValidator);
        when(mockValidator.supports("text")).thenReturn(true);
        when(formRepository.findById(10L)).thenReturn(Optional.of(mockForm));

        Map<Long, String> answers = Map.of(1L, "Nguyen Van A");

        submissionsService.submitForm(10L, "My", answers);

        verify(mockValidator, times(1)).validate(eq("Nguyen Van A"), any(Field.class));
        verify(submissionRepository, times(1)).save(any(Submission.class));
    }

    @Test
    @DisplayName("Hỗ trợ cả Key dạng String trong Map answers")
    void submitForm_ShouldHandleStringKeysInAnswersMap() {
        when(formRepository.findById(10L)).thenReturn(Optional.of(mockForm));

        Map<Long, String> answers = (Map) Map.of("1", "Gia tri test");

        assertDoesNotThrow(() ->
                submissionsService.submitForm(10L, "My", answers)
        );
    }

    @Test
    @DisplayName("Ném lỗi khi nộp dữ liệu không khớp với bất kỳ field nào")
    void submitForm_ShouldThrowException_WhenNoAnswersMatchFields() {
        when(formRepository.findById(10L)).thenReturn(Optional.of(mockForm));
        mockField.setRequired(false);

        // Gửi ID 99 trong khi Form chỉ có ID 1
        Map<Long, String> nonMatchingAnswers = Map.of(99L, "Gia tri");

        FormValidationException ex = assertThrows(FormValidationException.class, () ->
                submissionsService.submitForm(10L, "My", nonMatchingAnswers)
        );
        assertEquals("Dữ liệu nộp lên không khớp với bất kỳ câu hỏi nào trong Form.", ex.getMessage());
    }
}