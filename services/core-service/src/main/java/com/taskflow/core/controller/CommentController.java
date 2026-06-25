package com.taskflow.core.controller;

import com.taskflow.core.dto.CommentDto;
import com.taskflow.core.dto.CreateCommentRequest;
import com.taskflow.core.security.CurrentUser;
import com.taskflow.core.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks/{taskId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final CurrentUser currentUser;

    public CommentController(CommentService commentService, CurrentUser currentUser) {
        this.commentService = commentService;
        this.currentUser = currentUser;
    }

    @GetMapping
    public List<CommentDto> list(@PathVariable Long taskId) {
        return commentService.findByTask(taskId).stream().map(CommentDto::from).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@PathVariable Long taskId, @Valid @RequestBody CreateCommentRequest request) {
        Long authorId = currentUser.require().getId();
        return CommentDto.from(commentService.create(taskId, request, authorId));
    }
}
