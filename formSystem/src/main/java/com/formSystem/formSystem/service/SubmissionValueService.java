package com.formSystem.formSystem.service;

import com.formSystem.formSystem.model.SubmissionValue;
import com.formSystem.formSystem.repository.SubmissionValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubmissionValueService {

    @Autowired
    private SubmissionValueRepository submissionDataRepository;

    public List<SubmissionValue> getAllSubmissionData() {
        return submissionDataRepository.findAll();
    }

    public Optional<SubmissionValue> getSubmissionDataById(Long id) {
        return submissionDataRepository.findById(id);
    }

    public List<SubmissionValue> getSubmissionDataBySubmissionId(Long submissionId) {
        return submissionDataRepository.findBySubmissionId(submissionId);
    }

    public SubmissionValue saveSubmissionData(SubmissionValue submissionData) {
        return submissionDataRepository.save(submissionData);
    }

    public void deleteSubmissionValue(Long id) {
        submissionDataRepository.deleteById(id);
    }
}