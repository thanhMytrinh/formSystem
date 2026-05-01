package com.formSystem.formSystem.service;

import com.formSystem.formSystem.exception.FormValidationException;
import com.formSystem.formSystem.model.Field;
import com.formSystem.formSystem.model.Form;
import com.formSystem.formSystem.model.Submission;
import com.formSystem.formSystem.model.SubmissionValue;
import com.formSystem.formSystem.repository.FormsRepository;
import com.formSystem.formSystem.repository.SubmissionsRepository;
import com.formSystem.formSystem.util.FieldValidator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SubmissionsService {
    @Autowired
    private SubmissionsRepository submissionRepository;
    @Autowired

    private FormsRepository formRepository;
    @Autowired

    private List<FieldValidator> validators;

    @Transactional
    public void submitForm(Long formId, String employeeName, Map<Long, String> answers) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new EntityNotFoundException("Form không tồn tại"));

        if (form.getFields() == null || form.getFields().isEmpty()) {
            throw new FormValidationException("Hệ thống lỗi: Form này chưa được cấu hình câu hỏi.");
        }

        Submission submission = new Submission();
        submission.setForm(form);
        submission.setSubmittedBy(employeeName);
        System.out.println("Processing emp name: " + submission.getSubmittedBy());

        List<SubmissionValue> values = new ArrayList<>();

        for (Field field : form.getFields()) {
            System.out.println("Processing field: " + field.getLabel());

            String value = answers.get(field.getId());
            if (value == null) {
                value = answers.get(String.valueOf(field.getId()));
            }

            final String temp = value;
            validators.stream()
                    .filter(v -> v.supports(field.getType()))
                    .findFirst()
                    .ifPresent(v -> v.validate(temp, field));

            if (value != null && !value.isBlank()) {
                SubmissionValue subValue = new SubmissionValue();
                subValue.setSubmission(submission);
                subValue.setField(field);
                subValue.setValue(value);
                values.add(subValue);
            }
        }

        if (values.isEmpty() && !answers.isEmpty()) {
            throw new FormValidationException("Dữ liệu nộp lên không khớp với bất kỳ câu hỏi nào trong Form.");
        }

        submission.setValues(values);
        submissionRepository.save(submission);
    }





    public List<Submission> findAll() {
        return submissionRepository.findAll();
    }
}