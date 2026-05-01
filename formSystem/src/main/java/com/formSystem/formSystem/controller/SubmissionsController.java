package com.formSystem.formSystem.controller;

import com.formSystem.formSystem.dto.SubmissionRequest;
import com.formSystem.formSystem.model.Form;
import com.formSystem.formSystem.model.Submission;
import com.formSystem.formSystem.service.FormsService;
import com.formSystem.formSystem.service.SubmissionsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SubmissionsController {

    @Autowired
    private FormsService formService;

    @Autowired
    private SubmissionsService submissionService;

    @GetMapping("/forms/active")
    public ResponseEntity<List<Form>> getActiveForms() {
        return ResponseEntity.ok(formService.findActiveForms());
    }

    @PostMapping("/forms/{id}/submit")
    public ResponseEntity<String> submitForm(
            @PathVariable Long id,
            @RequestBody SubmissionRequest request) {
        submissionService.submitForm(id, request.getEmployeeName(), request.getAnswers());
        return ResponseEntity.ok("Đã gửi form thành công!");
    }

    @GetMapping("/submissions")
    public ResponseEntity<List<Submission>> getAllSubmissions() {
        return ResponseEntity.ok(submissionService.findAll());
    }
}