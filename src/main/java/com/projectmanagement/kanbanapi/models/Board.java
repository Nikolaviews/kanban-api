package com.projectmanagement.kanbanapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "boards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    private String id;

    private String name;

    private String ownerId; // Will store the ID of the User who owns the board

    @DBRef
    private List<Column> columns = new ArrayList<>();
}