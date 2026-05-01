package com.formSystem.formSystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldRequest {
    private String label;
    private String type;
    private Integer fieldOrder;
    private Boolean required;
    private String options;
    private Integer minValue;
    private Integer maxValue;
}
