package com.app.todolist.domain.todos;

import com.app.todolist.config.AuditingConfig;
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
class TodoRepositoryTest {

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
        Todo todo = Todo.create("todo-list", "hello");

        //when
        todosRepository.save(todo);
        Todo findTodo = todosRepository.findAll().stream().findAny().orElseThrow();

        //then
        Assertions.assertThat(findTodo.getTitle()).isEqualTo(todo.getTitle());
        Assertions.assertThat(findTodo.getContent()).isEqualTo(todo.getContent());

    }

    @Test
    @DisplayName("board에 저장한 시간이 createdAt에 저장된다")
    public void createdAtTest() {
        //given
        LocalDateTime now = LocalDateTime.now();
        Todo todo = Todo.create("todo-list", "hello");

        //when
        todosRepository.save(todo);
        Todo findTodo = todosRepository.findAll().stream().findFirst().orElseThrow();

        //then
        Assertions.assertThat(findTodo.getCreatedAt().isAfter(now)).isTrue();

    }

    @Test
    @DisplayName("board에 저장한 시간이 updatedAt에 저장된다")
    public void updatedAtTest() {
        //given
        LocalDateTime now = LocalDateTime.now();
        Todo todo = Todo.create("todo-list", "hello");

        //when
        todosRepository.save(todo);
        Todo findTodo = todosRepository.findAll().stream().findFirst().orElseThrow();

        //then
        Assertions.assertThat(findTodo.getUpdatedAt().isAfter(now)).isTrue();

    }


}