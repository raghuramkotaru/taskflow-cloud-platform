package com.taskflow.core.dto;

import com.taskflow.core.entity.Board;

import java.time.Instant;

public record BoardDto(
        Long id,
        String name,
        String description,
        Long ownerId,
        Instant createdAt) {

    public static BoardDto from(Board board) {
        return new BoardDto(
                board.getId(),
                board.getName(),
                board.getDescription(),
                board.getOwnerId(),
                board.getCreatedAt());
    }
}
