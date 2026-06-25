package com.taskflow.core;

import com.taskflow.core.dto.CreateTaskRequest;
import com.taskflow.core.dto.UpdateTaskRequest;
import com.taskflow.core.entity.Board;
import com.taskflow.core.entity.Task;
import com.taskflow.core.entity.TaskStatus;
import com.taskflow.core.repository.BoardRepository;
import com.taskflow.core.repository.TaskRepository;
import com.taskflow.core.service.ResourceNotFoundException;
import com.taskflow.core.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private TaskService taskService;

    private Board board;

    @BeforeEach
    void setup() {
        board = new Board("Board", "desc", 1L);
        board.setId(10L);
    }

    @Test
    void createPersistsTaskUnderBoard() {
        when(boardRepository.findById(10L)).thenReturn(Optional.of(board));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        Task created = taskService.create(10L, new CreateTaskRequest("Title", "Body", 5L));

        assertThat(created.getTitle()).isEqualTo("Title");
        assertThat(created.getBoard()).isSameAs(board);
        assertThat(created.getAssigneeId()).isEqualTo(5L);
        assertThat(created.getStatus()).isEqualTo(TaskStatus.TODO);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void createThrowsWhenBoardMissing() {
        when(boardRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.create(99L, new CreateTaskRequest("T", "B", null)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Board not found");
        verify(taskRepository, never()).save(any());
    }

    @Test
    void updateAppliesOnlyNonNullFields() {
        Task existing = new Task("Old", "OldDesc", board);
        existing.setId(1L);
        existing.setPriority(1);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        Task updated = taskService.update(1L,
                new UpdateTaskRequest(null, null, TaskStatus.DONE, 7, null));

        assertThat(updated.getTitle()).isEqualTo("Old");
        assertThat(updated.getStatus()).isEqualTo(TaskStatus.DONE);
        assertThat(updated.getPriority()).isEqualTo(7);
    }
}
