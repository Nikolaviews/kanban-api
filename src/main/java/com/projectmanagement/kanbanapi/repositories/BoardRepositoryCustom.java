package com.projectmanagement.kanbanapi.repositories;

import com.projectmanagement.kanbanapi.models.Board;
import java.util.Optional;

public interface BoardRepositoryCustom {
    Optional<Board> findByIdWithColumnsAndTasks(String boardId);
}