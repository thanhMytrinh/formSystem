package com.formSystem.formSystem.repository;

import com.formSystem.formSystem.model.Fields;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldsRepository extends JpaRepository<Fields, Long> {
    List<Fields> findByFormIdOrderByFieldOrder(Long formId);
}