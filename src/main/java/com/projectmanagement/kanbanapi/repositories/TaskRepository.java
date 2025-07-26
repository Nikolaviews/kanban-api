package com.projectmanagement.kanbanapi.repositories;

import com.projectmanagement.kanbanapi.models.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
}