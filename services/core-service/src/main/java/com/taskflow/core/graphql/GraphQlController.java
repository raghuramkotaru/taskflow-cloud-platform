package com.taskflow.core.graphql;

import com.taskflow.core.dto.CreateTaskRequest;
import com.taskflow.core.entity.Board;
import com.taskflow.core.entity.Comment;
import com.taskflow.core.entity.Task;
import com.taskflow.core.security.CurrentUser;
import com.taskflow.core.service.BoardService;
import com.taskflow.core.service.CommentService;
import com.taskflow.core.service.TaskService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class GraphQlController {

    private final BoardService boardService;
    private final TaskService taskService;
    private final CommentService commentService;
    private final CurrentUser currentUser;

    public GraphQlController(BoardService boardService,
                             TaskService taskService,
                             CommentService commentService,
                             CurrentUser currentUser) {
        this.boardService = boardService;
        this.taskService = taskService;
        this.commentService = commentService;
        this.currentUser = currentUser;
    }

    // Queries

    @QueryMapping
    public List<Board> boards() {
        return boardService.findAll();
    }

    @QueryMapping
    public Board board(@Argument Long id) {
        return boardService.findById(id);
    }

    @QueryMapping
    public Task task(@Argument Long id) {
        return taskService.findById(id);
    }

    // Nested resolvers

    @SchemaMapping(typeName = "Board", field = "tasks")
    public List<Task> boardTasks(Board board) {
        return taskService.findByBoard(board.getId());
    }

    @SchemaMapping(typeName = "Task", field = "comments")
    public List<Comment> taskComments(Task task) {
        return commentService.findByTask(task.getId());
    }

    @SchemaMapping(typeName = "Task", field = "boardId")
    public Long taskBoardId(Task task) {
        return task.getBoard().getId();
    }

    @SchemaMapping(typeName = "Comment", field = "taskId")
    public Long commentTaskId(Comment comment) {
        return comment.getTask().getId();
    }

    // Mutation (protected: requires a valid X-User-Id)

    @MutationMapping
    public Task createTask(@Argument Long boardId,
                           @Argument String title,
                           @Argument String description) {
        currentUser.require();
        return taskService.create(boardId, new CreateTaskRequest(title, description, null));
    }
}
