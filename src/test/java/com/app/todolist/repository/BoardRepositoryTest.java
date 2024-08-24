package com.app.todolist.repository;

import com.app.todolist.config.AuditingConfig;
import com.app.todolist.domain.board.Board;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(AuditingConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @AfterEach
    public void clear() {
        boardRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("BoardRepository 테스트")
    public void save() {
        //given
        String title = "todo-list";
        String content = "hello";

        //when
        Board board = Board.create(title, content);

        //then
        Assertions.assertThat(board.getTitle()).isEqualTo(title);
        Assertions.assertThat(board.getContent()).isEqualTo(content);

    }


}