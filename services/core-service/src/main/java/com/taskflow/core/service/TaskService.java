package com.taskflow.core.service;

import com.taskflow.core.dto.CreateTaskRequest;
import com.taskflow.core.dto.UpdateTaskRequest;
import com.taskflow.core.entity.Board;
import com.taskflow.core.entity.Task;
import com.taskflow.core.repository.BoardRepository;
import com.taskflow.core.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final BoardRepository boardRepository;

    public TaskService(TaskRepository taskRepository, BoardRepository boardRepository) {
        this.taskRepository = taskRepository;
        this.boardRepository = boardRepository;
    }

    @Transactional(readOnly = true)
    public List<Task> findByBoard(Long boardId) {
        return taskRepository.findByBoardId(boardId);
    }

    @Transactional(readOnly = true)
    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));
    }

    @Transactional
    public Task create(Long boardId, CreateTaskRequest request) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found: " + boardId));
        Task task = new Task(request.title(), request.description(), board);
        task.setAssigneeId(request.assigneeId());
        return taskRepository.save(task);
    }

    @Transactional
    public Task update(Long id, UpdateTaskRequest request) {
        Task task = findById(id);
        if (request.title() != null) {
            task.setTitle(request.title());
        }
        if (request.description() != null) {
            task.setDescription(request.description());
        }
        if (request.status() != null) {
            task.setStatus(request.status());
        }
        if (request.priority() != null) {
            task.setPriority(request.priority());
        }
        if (request.assigneeId() != null) {
            task.setAssigneeId(request.assigneeId());
        }
        return taskRepository.save(task);
    }

    @Transactional
    public void delete(Long id) {
        Task task = findById(id);
        taskRepository.delete(task);
    }
}
