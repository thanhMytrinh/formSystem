package com.formSystem.formSystem.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "submission_values")
@Data
public class SubmissionValues {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private Submissions submission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id", nullable = false)
    private Fields field;

    @Column(columnDefinition = "TEXT")
    private String value;
}