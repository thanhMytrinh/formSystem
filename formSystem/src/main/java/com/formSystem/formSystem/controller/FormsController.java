package com.formSystem.formSystem.controller;

import com.formSystem.formSystem.model.Forms;
import com.formSystem.formSystem.service.FormsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/forms")
public class FormsController {

    @Autowired
    private FormsService formsService;

    @GetMapping
    public List<Forms> getAllForms() {
        return formsService.getAllForms();
    }

    @GetMapping("/{id}")
    public Optional<Forms> getFormById(@PathVariable Long id) {
        return formsService.getFormById(id);
    }

    @PostMapping
    public Forms createForm(@RequestBody Forms form) {
        return formsService.saveForm(form);
    }

    @PutMapping("/{id}")
    public Forms updateForm(@PathVariable Long id, @RequestBody Forms form) {
        form.setId(id);
        return formsService.saveForm(form);
    }

    @DeleteMapping("/{id}")
    public void deleteForm(@PathVariable Long id) {
        formsService.deleteForm(id);
    }
}