package com.app.todolist.api.todos;

import com.app.todolist.api.todos.dto.TodoRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest
class TodoControllerTest {

    @Autowired
    private Validator validator;

    @Test
    @DisplayName("title을 입력하지 않을 경우 예외가 발생한다.")
    public void titleValidTest() {
        // given
        TodoRequest todoRequest = new TodoRequest();
        todoRequest.setTitle(null);
        todoRequest.setContent("tao content");

        // when
        Set<ConstraintViolation<TodoRequest>> violations = validator.validate(todoRequest);

        // then
        Assertions.assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("제목을 입력하세요.")
                .hasSize(1);
    }

    @Test
    @DisplayName("title을 입력할 경우 예외가 발생하지 않는다.")
    public void successTest() {
        // given
        TodoRequest todoRequest = new TodoRequest();
        todoRequest.setTitle("tao title");
        todoRequest.setContent("tao content");

        // when
        Set<ConstraintViolation<TodoRequest>> violations = validator.validate(todoRequest);

        // then
        Assertions.assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains()
                .hasSize(0);
    }
}
