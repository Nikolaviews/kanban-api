package com.projectmanagement.kanbanapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnOrderRequest {
    private String columnId;
    private List<String> taskIds;
}