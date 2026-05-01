package com.formSystem.formSystem.service;

import com.formSystem.formSystem.exception.ValidationException;
import com.formSystem.formSystem.model.Field;
import com.formSystem.formSystem.model.Form;
import com.formSystem.formSystem.repository.FieldsRepository;
import com.formSystem.formSystem.repository.FormsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FieldsService {
    @Autowired
    private FieldsRepository fieldRepository;
    @Autowired
    private FormsRepository formRepository;

    public Field addFieldToForm(Long formId, Field field) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new EntityNotFoundException("Form này chưa tồn tại"));
        validateField(field);
        field.setForm(form);
        return fieldRepository.save(field);
    }

    public Field updateField(Long fieldId, Field details) {
        Field existing = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new EntityNotFoundException("Form này chưa tồn tại"));
        validateField(details);
        existing.setLabel(details.getLabel());
        existing.setType(details.getType());
        existing.setFieldOrder(details.getFieldOrder());
        existing.setRequired(details.getRequired());
        existing.setOptions(details.getOptions());
        existing.setMinValue(details.getMinValue());
        existing.setMaxValue(details.getMaxValue());
        return fieldRepository.save(existing);
    }

    public void deleteField(Long fieldId) {
        fieldRepository.deleteById(fieldId);
    }

    /**
     * Reorder fields theo danh sách fieldIds được cung cấp
     * @param formId Form ID
     * @param fieldIds Danh sách field IDs theo thứ tự mới (từ trên xuống)
     */
    public void reorderFields(Long formId, List<Long> fieldIds) {
        // Verify form exists
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new EntityNotFoundException("Form not found"));

        // Verify all field IDs exist and belong to this form
        for (int i = 0; i < fieldIds.size(); i++) {
            final int index = i;
            Long fieldId = fieldIds.get(i);
            Field field = fieldRepository.findById(fieldId)
                    .orElseThrow(() -> new EntityNotFoundException("Field with ID " + fieldId + " not found"));
            
            if (!field.getForm().getId().equals(formId)) {
                throw new ValidationException("Field with ID " + fieldId + " does not belong to this form");
            }
            
            // Update field order
            field.setFieldOrder(index);
            fieldRepository.save(field);
        }
    }

    private void validateField(Field field) {
        // Validate label
        if (field.getLabel() == null || field.getLabel().isBlank()) {
            throw new ValidationException("Label không được để trống");
        }
        
        // Validate type
        String type = field.getType();
        if (type == null || type.isBlank()) {
            throw new ValidationException("Type không được để trống");
        }
        
        String[] validTypes = {"text", "number", "date", "color", "select"};
        boolean isValidType = false;
        for (String validType : validTypes) {
            if (validType.equalsIgnoreCase(type)) {
                isValidType = true;
                break;
            }
        }
        if (!isValidType) {
            throw new ValidationException("Type phải là một trong: text, number, date, color, select");
        }
        
        // Validate min/max values
        if (field.getMinValue() != null && field.getMaxValue() != null 
                && field.getMinValue() > field.getMaxValue()) {
            throw new ValidationException("minValue không thể lớn hơn maxValue");
        }
        
        // Validate options for select type
        if ("select".equalsIgnoreCase(type)) {
            if (field.getOptions() == null || field.getOptions().isBlank()) {
                throw new ValidationException("Field select phải có options");
            }
            // Check if it's valid JSON format
            if (!field.getOptions().startsWith("[") || !field.getOptions().endsWith("]")) {
                throw new ValidationException("Options phải là JSON array format (e.g., [\"A\", \"B\"])");
            }
        }
    }
}