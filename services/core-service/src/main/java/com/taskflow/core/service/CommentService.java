package com.taskflow.core.service;

import com.taskflow.core.dto.CreateCommentRequest;
import com.taskflow.core.entity.Comment;
import com.taskflow.core.entity.Task;
import com.taskflow.core.repository.CommentRepository;
import com.taskflow.core.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    public CommentService(CommentRepository commentRepository, TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public List<Comment> findByTask(Long taskId) {
        return commentRepository.findByTaskId(taskId);
    }

    @Transactional
    public Comment create(Long taskId, CreateCommentRequest request, Long authorId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + taskId));
        Comment comment = new Comment(request.body(), authorId, task);
        return commentRepository.save(comment);
    }
}
