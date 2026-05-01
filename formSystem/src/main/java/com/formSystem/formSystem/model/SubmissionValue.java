package com.formSystem.formSystem.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "submission_values")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class SubmissionValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private Submission submission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id", nullable = false)
    private Field field;

    @Column(columnDefinition = "TEXT")
    private String value;
}