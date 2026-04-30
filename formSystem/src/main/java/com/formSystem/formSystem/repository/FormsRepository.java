package com.formSystem.formSystem.repository;

import com.formSystem.formSystem.model.Forms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormsRepository extends JpaRepository<Forms, Long> {
}