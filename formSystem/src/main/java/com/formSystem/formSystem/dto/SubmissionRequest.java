package com.formSystem.formSystem.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter @Setter
public class SubmissionRequest {
    private String employeeName;
    private Map<Long, String> answers;
}