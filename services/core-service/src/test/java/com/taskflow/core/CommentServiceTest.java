package com.taskflow.core;

import com.taskflow.core.dto.CreateCommentRequest;
import com.taskflow.core.entity.Board;
import com.taskflow.core.entity.Comment;
import com.taskflow.core.entity.Task;
import com.taskflow.core.repository.CommentRepository;
import com.taskflow.core.repository.TaskRepository;
import com.taskflow.core.service.CommentService;
import com.taskflow.core.service.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    void createAttachesCommentToTaskWithAuthor() {
        Board board = new Board("B", "d", 1L);
        board.setId(1L);
        Task task = new Task("T", "d", board);
        task.setId(42L);
        when(taskRepository.findById(42L)).thenReturn(Optional.of(task));
        when(commentRepository.save(any(Comment.class))).thenAnswer(inv -> inv.getArgument(0));

        Comment created = commentService.create(42L, new CreateCommentRequest("Nice work"), 7L);

        assertThat(created.getBody()).isEqualTo("Nice work");
        assertThat(created.getAuthorId()).isEqualTo(7L);
        assertThat(created.getTask()).isSameAs(task);
    }

    @Test
    void createThrowsWhenTaskMissing() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.create(1L, new CreateCommentRequest("x"), 1L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(commentRepository, never()).save(any());
    }
}
