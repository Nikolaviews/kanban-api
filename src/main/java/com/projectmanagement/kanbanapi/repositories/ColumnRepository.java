package com.projectmanagement.kanbanapi.repositories;

import com.projectmanagement.kanbanapi.models.Column;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColumnRepository extends MongoRepository<Column, String> {
}