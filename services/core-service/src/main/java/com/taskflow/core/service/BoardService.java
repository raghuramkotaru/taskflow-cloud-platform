package com.taskflow.core.service;

import com.taskflow.core.dto.CreateBoardRequest;
import com.taskflow.core.entity.Board;
import com.taskflow.core.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional(readOnly = true)
    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found: " + id));
    }

    @Transactional
    public Board create(CreateBoardRequest request, Long ownerId) {
        Board board = new Board(request.name(), request.description(), ownerId);
        return boardRepository.save(board);
    }

    @Transactional
    public void delete(Long id, Long requesterId) {
        Board board = findById(id);
        if (!board.getOwnerId().equals(requesterId)) {
            throw new com.taskflow.core.security.UnauthorizedException(
                    "Only the board owner may delete it");
        }
        boardRepository.delete(board);
    }
}
