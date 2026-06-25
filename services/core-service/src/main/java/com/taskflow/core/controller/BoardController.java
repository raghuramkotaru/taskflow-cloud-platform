package com.taskflow.core.controller;

import com.taskflow.core.dto.BoardDto;
import com.taskflow.core.dto.CreateBoardRequest;
import com.taskflow.core.entity.Board;
import com.taskflow.core.security.CurrentUser;
import com.taskflow.core.service.BoardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/boards")
public class BoardController {

    private final BoardService boardService;
    private final CurrentUser currentUser;

    public BoardController(BoardService boardService, CurrentUser currentUser) {
        this.boardService = boardService;
        this.currentUser = currentUser;
    }

    @GetMapping
    public List<BoardDto> list() {
        return boardService.findAll().stream().map(BoardDto::from).toList();
    }

    @GetMapping("/{id}")
    public BoardDto get(@PathVariable Long id) {
        return BoardDto.from(boardService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BoardDto create(@Valid @RequestBody CreateBoardRequest request) {
        Long ownerId = currentUser.require().getId();
        Board board = boardService.create(request, ownerId);
        return BoardDto.from(board);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Long requesterId = currentUser.require().getId();
        boardService.delete(id, requesterId);
        return ResponseEntity.noContent().build();
    }
}
