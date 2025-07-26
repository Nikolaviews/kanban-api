package com.projectmanagement.kanbanapi.services;

import com.projectmanagement.kanbanapi.dto.ColumnOrderRequest;
import com.projectmanagement.kanbanapi.models.Board;
import com.projectmanagement.kanbanapi.models.Column;
import com.projectmanagement.kanbanapi.models.Task;
import com.projectmanagement.kanbanapi.models.User;
import com.projectmanagement.kanbanapi.repositories.BoardRepository;
import com.projectmanagement.kanbanapi.repositories.ColumnRepository;
import com.projectmanagement.kanbanapi.repositories.TaskRepository;
import com.projectmanagement.kanbanapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    private final ColumnRepository columnRepository;
    private final TaskRepository taskRepository;

    private final WebSocketService webSocketService;

    public Board createBoard(String boardName, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        Board board = new Board();
        board.setName(boardName);
        board.setOwnerId(user.getId());
        return boardRepository.save(board);
    }

    public List<Board> getBoardsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return boardRepository.findByOwnerId(user.getId());
    }

    public void deleteBoard(String boardId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalStateException("Board not found: " + boardId));

        if (!board.getOwnerId().equals(user.getId())) {
            throw new IllegalStateException("User does not have permission to delete this board");
        }
        // Note: In a real app, you would also delete associated columns and tasks.
        // For now, we'll just delete the board itself.
        boardRepository.delete(board);
    }

    public Optional<Board> getBoardDetails(String boardId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        Optional<Board> boardOpt = boardRepository.findByIdWithColumnsAndTasks(boardId);
        if (boardOpt.isPresent()) {
            Board board = boardOpt.get();
            if (!board.getOwnerId().equals(user.getId())) {
                throw new IllegalStateException("User does not have permission to view this board");
            }
        }
        return boardOpt;
    }

    @Transactional
    public Column createColumnForBoard(String boardId, String columnTitle) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalStateException("Board not found"));

        Column column = new Column();
        column.setTitle(columnTitle);
        column.setTasks(List.of()); // Ensure tasks list is not null
        Column savedColumn = columnRepository.save(column);

        board.getColumns().add(savedColumn);
        boardRepository.save(board);

        // After saving, notify clients for this board
        webSocketService.notifyBoardUpdated(boardId);
        return savedColumn;
    }

    @Transactional
    public Task createTaskForColumn(String columnId, String taskTitle) {
        Column column = columnRepository.findById(columnId)
                .orElseThrow(() -> new IllegalStateException("Column not found"));

        Task task = new Task();
        task.setTitle(taskTitle);
        Task savedTask = taskRepository.save(task);

        column.getTasks().add(savedTask);
        columnRepository.save(column);

        // After saving, find the board and send a notification
        String boardId = findBoardIdForColumn(columnId);
        webSocketService.notifyBoardUpdated(boardId);
        return savedTask;
    }

    // We pass the boardId in now to avoid an extra DB query
    @Transactional
    public void updateColumnAndTaskOrder(String boardId, List<ColumnOrderRequest> requests) {
        List<String> columnIds = requests.stream().map(ColumnOrderRequest::getColumnId).toList();
        List<String> taskIds = requests.stream().flatMap(req -> req.getTaskIds().stream()).toList();

        List<Column> columns = columnRepository.findAllById(columnIds);
        List<Task> tasks = taskRepository.findAllById(taskIds);

        Map<String, Column> columnMap = columns.stream().collect(Collectors.toMap(Column::getId, c -> c));
        Map<String, Task> taskMap = tasks.stream().collect(Collectors.toMap(Task::getId, t -> t));

        for (ColumnOrderRequest request : requests) {
            Column column = columnMap.get(request.getColumnId());
            if (column != null) {
                List<Task> orderedTasks = request.getTaskIds().stream()
                        .map(taskMap::get)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                column.setTasks(orderedTasks);
                columnRepository.save(column);
            }
        }
        // After all updates are done, notify clients for this board
        webSocketService.notifyBoardUpdated(boardId);
    }

    // Helper method to find which board a column belongs to
    private String findBoardIdForColumn(String columnId) {
        return boardRepository.findBoardByColumnId(columnId).map(Board::getId).orElse(null);
    }
}