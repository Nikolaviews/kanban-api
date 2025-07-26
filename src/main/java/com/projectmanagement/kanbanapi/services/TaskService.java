package com.projectmanagement.kanbanapi.services;

import com.projectmanagement.kanbanapi.dto.UpdateTaskRequest;
import com.projectmanagement.kanbanapi.models.Board;
import com.projectmanagement.kanbanapi.models.Task;
import com.projectmanagement.kanbanapi.repositories.BoardRepository;
import com.projectmanagement.kanbanapi.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final BoardRepository boardRepository;
    private final WebSocketService webSocketService;

    @Transactional
    public Task updateTask(String taskId, UpdateTaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalStateException("Task not found with id: " + taskId));

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }

        Task savedTask = taskRepository.save(task);

        // After saving, find the board and send a notification
        String boardId = findBoardIdForTask(taskId);
        webSocketService.notifyBoardUpdated(boardId);

        return savedTask;
    }

    // Helper method to find which board a task belongs to
    private String findBoardIdForTask(String taskId) {
        return boardRepository.findBoardByTaskId(taskId).map(Board::getId).orElse(null);
    }
}