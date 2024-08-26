package com.app.todolist.repository;

import com.app.todolist.config.AuditingConfig;
import com.app.todolist.domain.board.Board;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

@DataJpaTest
@Import(AuditingConfig.class)
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @AfterEach
    public void clear() {
        boardRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("board에 저장한 값을 조회할 수 있다")
    public void saveTest() {
        //given
        Board board = Board.create("todo-list", "hello");

        //when
        boardRepository.save(board);
        Board findBoard = boardRepository.findAll().stream().findAny().orElseThrow();

        //then
        Assertions.assertThat(findBoard.getTitle()).isEqualTo(board.getTitle());
        Assertions.assertThat(findBoard.getContent()).isEqualTo(board.getContent());

    }

    @Test
    @DisplayName("board에 저장한 시간이 createdAt에 저장된다")
    public void createdAtTest() {
        //given
        LocalDateTime now = LocalDateTime.now();
        Board board = Board.create("todo-list", "hello");

        //when
        boardRepository.save(board);
        Board findBoard = boardRepository.findAll().stream().findFirst().orElseThrow();

        //then
        Assertions.assertThat(findBoard.getCreatedAt().isAfter(now)).isTrue();

    }

    @Test
    @DisplayName("board에 저장한 시간이 updatedAt에 저장된다")
    public void updatedAtTest() {
        //given
        LocalDateTime now = LocalDateTime.now();
        Board board = Board.create("todo-list", "hello");

        //when
        boardRepository.save(board);
        Board findBoard = boardRepository.findAll().stream().findFirst().orElseThrow();

        //then
        Assertions.assertThat(findBoard.getUpdatedAt().isAfter(now)).isTrue();

    }


}