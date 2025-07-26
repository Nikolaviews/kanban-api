package com.projectmanagement.kanbanapi.controllers;

import com.projectmanagement.kanbanapi.dto.UpdateTaskRequest;
import com.projectmanagement.kanbanapi.models.Task;
import com.projectmanagement.kanbanapi.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(
            @PathVariable String taskId,
            @RequestBody UpdateTaskRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        // Production security: Add check to ensure userDetails has permission for this taskId
        Task updatedTask = taskService.updateTask(taskId, request);
        return ResponseEntity.ok(updatedTask);
    }
}