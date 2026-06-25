package com.taskflow.core.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateTaskRequest(
        @NotBlank String title,
        String description,
        Long assigneeId) {
}
