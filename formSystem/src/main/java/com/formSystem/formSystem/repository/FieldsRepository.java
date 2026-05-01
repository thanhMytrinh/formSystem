package com.formSystem.formSystem.repository;

import com.formSystem.formSystem.model.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldsRepository extends JpaRepository<Field, Long> {
    List<Field> findByFormIdOrderByFieldOrderAsc(Long formId);
}