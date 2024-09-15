package com.app.todolist.repository;

import com.app.todolist.config.AuditingConfig;
import com.app.todolist.domain.todos.Todos;
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
class TodosRepositoryTest {

    @Autowired
    private TodosRepository todosRepository;

    @AfterEach
    public void clear() {
        todosRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("board에 저장한 값을 조회할 수 있다")
    public void saveTest() {
        //given
        Todos todos = Todos.create("todo-list", "hello");

        //when
        todosRepository.save(todos);
        Todos findTodos = todosRepository.findAll().stream().findAny().orElseThrow();

        //then
        Assertions.assertThat(findTodos.getTitle()).isEqualTo(todos.getTitle());
        Assertions.assertThat(findTodos.getContent()).isEqualTo(todos.getContent());

    }

    @Test
    @DisplayName("board에 저장한 시간이 createdAt에 저장된다")
    public void createdAtTest() {
        //given
        LocalDateTime now = LocalDateTime.now();
        Todos todos = Todos.create("todo-list", "hello");

        //when
        todosRepository.save(todos);
        Todos findTodos = todosRepository.findAll().stream().findFirst().orElseThrow();

        //then
        Assertions.assertThat(findTodos.getCreatedAt().isAfter(now)).isTrue();

    }

    @Test
    @DisplayName("board에 저장한 시간이 updatedAt에 저장된다")
    public void updatedAtTest() {
        //given
        LocalDateTime now = LocalDateTime.now();
        Todos todos = Todos.create("todo-list", "hello");

        //when
        todosRepository.save(todos);
        Todos findTodos = todosRepository.findAll().stream().findFirst().orElseThrow();

        //then
        Assertions.assertThat(findTodos.getUpdatedAt().isAfter(now)).isTrue();

    }


}