package com.projectmanagement.kanbanapi.controllers;

import com.projectmanagement.kanbanapi.dto.ColumnOrderRequest;
import com.projectmanagement.kanbanapi.dto.CreateBoardRequest;
import com.projectmanagement.kanbanapi.dto.CreateColumnRequest;
import com.projectmanagement.kanbanapi.dto.CreateTaskRequest;
import com.projectmanagement.kanbanapi.models.Board;
import com.projectmanagement.kanbanapi.models.Column;
import com.projectmanagement.kanbanapi.models.Task;
import com.projectmanagement.kanbanapi.services.BoardService;
import com.projectmanagement.kanbanapi.services.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    private final WebSocketService webSocketService;

    @PostMapping
    public ResponseEntity<Board> createBoard(@RequestBody CreateBoardRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        Board newBoard = boardService.createBoard(request.getName(), userDetails.getUsername());
        return ResponseEntity.ok(newBoard);
    }

    @GetMapping
    public ResponseEntity<List<Board>> getUserBoards(@AuthenticationPrincipal UserDetails userDetails) {
        List<Board> boards = boardService.getBoardsForUser(userDetails.getUsername());
        return ResponseEntity.ok(boards);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable String boardId, @AuthenticationPrincipal UserDetails userDetails) {
        boardService.deleteBoard(boardId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<Board> getBoardById(@PathVariable String boardId, @AuthenticationPrincipal UserDetails userDetails) {
        return boardService.getBoardDetails(boardId, userDetails.getUsername())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{boardId}/columns")
    public ResponseEntity<Column> createColumn(@PathVariable String boardId, @RequestBody CreateColumnRequest request) {
        return ResponseEntity.ok(boardService.createColumnForBoard(boardId, request.getTitle()));
    }

    @PostMapping("/columns/{columnId}/tasks")
    public ResponseEntity<Task> createTask(@PathVariable String columnId, @RequestBody CreateTaskRequest request) {
        return ResponseEntity.ok(boardService.createTaskForColumn(columnId, request.getTitle()));
    }

    @PutMapping("/{boardId}/columns/order")
    public ResponseEntity<Void> updateColumnsOrder(
            @PathVariable String boardId,
            @RequestBody List<ColumnOrderRequest> requests,
            @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("In updateColumnsOrder: " + boardId);
        boardService.updateColumnAndTaskOrder(boardId, requests);
        return ResponseEntity.ok().build();
    }
    //688484273b46bb2f49da1c3d/columns/order
}