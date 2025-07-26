package com.projectmanagement.kanbanapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "columns")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Column {

    @Id
    private String id;

    private String title;

    @DBRef
    private List<Task> tasks = new ArrayList<>();
}