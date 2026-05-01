package com.formSystem.formSystem.service;

import com.formSystem.formSystem.model.Form;
import com.formSystem.formSystem.repository.FormsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FormsService {

    @Autowired
    private FormsRepository formsRepository;
    public Page<Form> findAll(Pageable pageable) {
        return formsRepository.findAll(pageable);
    }

    public List<Form> findActiveForms() {
        return formsRepository.findByStatusOrderByDisplayOrderAsc("ACTIVE");
    }

    public Form findById(Long id) {
        return formsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Form ID: " + id));
    }

    public Form save(Form form) {
        return formsRepository.save(form);
    }

    public Form update(Long id, Form details) {
        Form existing = findById(id);
        existing.setTitle(details.getTitle());
        existing.setDescription(details.getDescription());
        existing.setDisplayOrder(details.getDisplayOrder());
        existing.setStatus(details.getStatus());
        return formsRepository.save(existing);
    }

    public void delete(Long id) {
        formsRepository.deleteById(id);
    }
}