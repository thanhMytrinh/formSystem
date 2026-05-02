package com.formSystem.formSystem.service;

import com.formSystem.formSystem.model.Form;
import com.formSystem.formSystem.repository.FormsRepository;
import com.formSystem.formSystem.service.FormsService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FormsServiceTest {

    @Mock
    private FormsRepository formsRepository;

    @InjectMocks
    private FormsService formsService;

    private Form sampleForm;

    @BeforeEach
    void setUp() {
        sampleForm = new Form();
        sampleForm.setId(1L);
        sampleForm.setTitle("Khảo sát nhân viên");
        sampleForm.setStatus("ACTIVE");
        sampleForm.setDisplayOrder(1);
    }

    @Test
    @DisplayName("Nên trả về trang danh sách Form khi gọi findAll")
    void findAll_ShouldReturnPageOfForms() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Form> page = new PageImpl<>(List.of(sampleForm));

        when(formsRepository.findAll(pageable)).thenReturn(page);

        Page<Form> result = formsService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(formsRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Nên trả về danh sách Form ACTIVE đã sắp xếp")
    void findActiveForms_ShouldReturnActiveForms() {
        when(formsRepository.findByStatusOrderByDisplayOrderAsc("ACTIVE"))
                .thenReturn(List.of(sampleForm));

        List<Form> result = formsService.findActiveForms();

        assertEquals(1, result.size());
        assertEquals("ACTIVE", result.get(0).getStatus());
    }

    @Test
    @DisplayName("Nên trả về Form khi ID tồn tại")
    void findById_ShouldReturnForm_WhenIdExists() {
        when(formsRepository.findById(1L)).thenReturn(Optional.of(sampleForm));

        Form result = formsService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("Nên ném lỗi EntityNotFound khi ID không tồn tại")
    void findById_ShouldThrowException_WhenIdDoesNotExist() {
        when(formsRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> formsService.findById(99L));
    }

    @Test
    @DisplayName("Nên lưu Form mới khi gọi save")
    void save_ShouldReturnSavedForm() {
        when(formsRepository.save(any(Form.class))).thenReturn(sampleForm);

        Form result = formsService.save(new Form());

        assertNotNull(result);
        assertEquals("Khảo sát nhân viên", result.getTitle());
    }

    @Test
    @DisplayName("Nên cập nhật thông tin và lưu lại khi gọi update")
    void update_ShouldUpdateAndSaveForm() {
        Form details = new Form();
        details.setTitle("Tiêu đề mới");
        details.setStatus("INACTIVE");

        when(formsRepository.findById(1L)).thenReturn(Optional.of(sampleForm));
        when(formsRepository.save(any(Form.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Form updatedForm = formsService.update(1L, details);

        assertEquals("Tiêu đề mới", updatedForm.getTitle());
        assertEquals("INACTIVE", updatedForm.getStatus());
        verify(formsRepository).save(sampleForm);
    }

    @Test
    @DisplayName("Nên gọi repository.deleteById khi xóa Form")
    void delete_ShouldInvokeDeleteOnRepository() {
        doNothing().when(formsRepository).deleteById(1L);

        formsService.delete(1L);

        verify(formsRepository, times(1)).deleteById(1L);
    }
}