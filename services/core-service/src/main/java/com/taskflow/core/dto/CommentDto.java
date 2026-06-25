package com.taskflow.core.dto;

import com.taskflow.core.entity.Comment;

import java.time.Instant;

public record CommentDto(
        Long id,
        String body,
        Long authorId,
        Long taskId,
        Instant createdAt) {

    public static CommentDto from(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getBody(),
                comment.getAuthorId(),
                comment.getTask().getId(),
                comment.getCreatedAt());
    }
}
