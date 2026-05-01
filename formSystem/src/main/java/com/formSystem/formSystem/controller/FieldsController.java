package com.formSystem.formSystem.controller;

import com.formSystem.formSystem.dto.FieldReorderRequest;
import com.formSystem.formSystem.dto.FieldRequest;
import com.formSystem.formSystem.model.Field;
import com.formSystem.formSystem.service.FieldsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/forms/{id}/fields")
public class FieldsController {

    @Autowired
    private FieldsService fieldService;

    @PostMapping
    @Operation(summary = "Thêm field vào form")
    public ResponseEntity<Field> addField(@PathVariable("id") Long id, @RequestBody FieldRequest fieldRequest) {
        Field field = toField(fieldRequest);
        return ResponseEntity.ok(fieldService.addFieldToForm(id, field));
    }

    @PutMapping("/{fid}")
    public ResponseEntity<Field> updateField(
            @PathVariable("id") Long id,
            @PathVariable("fid") Long fid,
            @RequestBody FieldRequest fieldRequest) {
        Field fieldDetails = toField(fieldRequest);
        return ResponseEntity.ok(fieldService.updateField(fid, fieldDetails));
    }

    @DeleteMapping("/{fid}")
    public ResponseEntity<Void> deleteField(@PathVariable("id") Long id, @PathVariable("fid") Long fid) {
        fieldService.deleteField(fid);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/reorder")
    @Operation(summary = "Sắp xếp lại thứ tự các field trong form")
    public ResponseEntity<String> reorderFields(@PathVariable("id") Long id, @RequestBody FieldReorderRequest request) {
        fieldService.reorderFields(id, request.getFieldIds());
        return ResponseEntity.ok("Chỉnh sửa thứ tự field thành công!");
    }

    private Field toField(FieldRequest request) {
        Field field = new Field();
        field.setLabel(request.getLabel());
        field.setType(request.getType());
        field.setFieldOrder(request.getFieldOrder());
        field.setRequired(request.getRequired());
        field.setOptions(request.getOptions());
        field.setMinValue(request.getMinValue());
        field.setMaxValue(request.getMaxValue());
        return field;
    }
}