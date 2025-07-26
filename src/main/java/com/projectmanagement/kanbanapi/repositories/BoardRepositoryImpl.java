package com.projectmanagement.kanbanapi.repositories;

import com.projectmanagement.kanbanapi.models.Board;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    public BoardRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Optional<Board> findByIdWithColumnsAndTasks(String boardId) {
        Board board = mongoTemplate.findById(boardId, Board.class);
        return Optional.ofNullable(board);
    }
}