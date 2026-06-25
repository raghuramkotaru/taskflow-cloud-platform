package com.taskflow.core.controller;

import com.taskflow.core.dto.CreateTaskRequest;
import com.taskflow.core.dto.TaskDto;
import com.taskflow.core.dto.UpdateTaskRequest;
import com.taskflow.core.security.CurrentUser;
import com.taskflow.core.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TaskController {

    private final TaskService taskService;
    private final CurrentUser currentUser;

    public TaskController(TaskService taskService, CurrentUser currentUser) {
        this.taskService = taskService;
        this.currentUser = currentUser;
    }

    @GetMapping("/boards/{boardId}/tasks")
    public List<TaskDto> listByBoard(@PathVariable Long boardId) {
        return taskService.findByBoard(boardId).stream().map(TaskDto::from).toList();
    }

    @GetMapping("/tasks/{id}")
    public TaskDto get(@PathVariable Long id) {
        return TaskDto.from(taskService.findById(id));
    }

    @PostMapping("/boards/{boardId}/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto create(@PathVariable Long boardId, @Valid @RequestBody CreateTaskRequest request) {
        currentUser.require();
        return TaskDto.from(taskService.create(boardId, request));
    }

    @PatchMapping("/tasks/{id}")
    public TaskDto update(@PathVariable Long id, @RequestBody UpdateTaskRequest request) {
        currentUser.require();
        return TaskDto.from(taskService.update(id, request));
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        currentUser.require();
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
