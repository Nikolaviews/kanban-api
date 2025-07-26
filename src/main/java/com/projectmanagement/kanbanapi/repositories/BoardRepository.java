package com.projectmanagement.kanbanapi.repositories;

import com.projectmanagement.kanbanapi.models.Board;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends MongoRepository<Board, String>, BoardRepositoryCustom {
    List<Board> findByOwnerId(String ownerId);

    @Query(value = "{ 'columns.tasks._id': ?0 }", fields = "{ '_id': 1 }")
    Optional<Board> findBoardByTaskId(String taskId);

    @Query(value = "{ 'columns._id': ?0 }", fields = "{ '_id': 1 }")
    Optional<Board> findBoardByColumnId(String columnId);
}