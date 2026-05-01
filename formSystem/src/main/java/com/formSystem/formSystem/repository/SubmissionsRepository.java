package com.formSystem.formSystem.repository;

import com.formSystem.formSystem.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionsRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByFormId(Long formId);
}