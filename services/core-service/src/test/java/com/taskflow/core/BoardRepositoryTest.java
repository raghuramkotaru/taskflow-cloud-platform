package com.taskflow.core;

import com.taskflow.core.entity.Board;
import com.taskflow.core.entity.User;
import com.taskflow.core.repository.BoardRepository;
import com.taskflow.core.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableAutoConfiguration(exclude = FlywayAutoConfiguration.class)
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void savesAndFindsBoardsByOwner() {
        User owner = userRepository.save(new User("carol", "Carol"));

        boardRepository.save(new Board("Roadmap", "Q3 roadmap", owner.getId()));
        boardRepository.save(new Board("Backlog", "Ideas", owner.getId()));
        boardRepository.save(new Board("Other", "Someone else", owner.getId() + 999));

        List<Board> boards = boardRepository.findByOwnerId(owner.getId());

        assertThat(boards).hasSize(2);
        assertThat(boards).extracting(Board::getName)
                .containsExactlyInAnyOrder("Roadmap", "Backlog");
    }

    @Test
    void persistsCreatedAtTimestamp() {
        User owner = userRepository.save(new User("dave", "Dave"));
        Board saved = boardRepository.save(new Board("Sprint", null, owner.getId()));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
    }
}
