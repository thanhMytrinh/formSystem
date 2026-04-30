package com.formSystem.formSystem.controller;

import com.formSystem.formSystem.model.SubmissionValues;
import com.formSystem.formSystem.service.SubmissionValuesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/submission-values")
public class SubmissionValuesController {

    @Autowired
    private SubmissionValuesService submissionValuesService;

    @GetMapping
    public List<SubmissionValues> getAllSubmissionValues() {
        return submissionValuesService.getAllSubmissionValues();
    }

    @GetMapping("/{id}")
    public Optional<SubmissionValues> getSubmissionValueById(@PathVariable Long id) {
        return submissionValuesService.getSubmissionValueById(id);
    }

    @GetMapping("/submission/{submissionId}")
    public List<SubmissionValues> getSubmissionValuesBySubmissionId(@PathVariable Long submissionId) {
        return submissionValuesService.getSubmissionValuesBySubmissionId(submissionId);
    }

    @PostMapping
    public SubmissionValues createSubmissionValue(@RequestBody SubmissionValues submissionValue) {
        return submissionValuesService.saveSubmissionValue(submissionValue);
    }

    @PutMapping("/{id}")
    public SubmissionValues updateSubmissionValue(@PathVariable Long id, @RequestBody SubmissionValues submissionValue) {
        submissionValue.setId(id);
        return submissionValuesService.saveSubmissionValue(submissionValue);
    }

    @DeleteMapping("/{id}")
    public void deleteSubmissionValue(@PathVariable Long id) {
        submissionValuesService.deleteSubmissionValue(id);
    }
}