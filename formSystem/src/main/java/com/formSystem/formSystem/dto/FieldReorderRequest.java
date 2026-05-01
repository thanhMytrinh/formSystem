package com.formSystem.formSystem.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter @Setter
public class FieldReorderRequest {
    /**
     * Danh sách field IDs theo thứ tự mới
     * VD: [3, 1, 2] - field 3 sẽ là order 0, field 1 là order 1, field 2 là order 2
     */
    private List<Long> fieldIds;
}
