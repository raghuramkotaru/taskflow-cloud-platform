package com.taskflow.core.dto;

import com.taskflow.core.entity.Task;
import com.taskflow.core.entity.TaskStatus;

import java.time.Instant;

public record TaskDto(
        Long id,
        String title,
        String description,
        TaskStatus status,
        int priority,
        Long boardId,
        Long assigneeId,
        Instant createdAt) {

    public static TaskDto from(Task task) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getBoard().getId(),
                task.getAssigneeId(),
                task.getCreatedAt());
    }
}
