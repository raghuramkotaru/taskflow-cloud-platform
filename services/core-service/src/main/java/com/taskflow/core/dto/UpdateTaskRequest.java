package com.taskflow.core.dto;

import com.taskflow.core.entity.TaskStatus;

public record UpdateTaskRequest(
        String title,
        String description,
        TaskStatus status,
        Integer priority,
        Long assigneeId) {
}
