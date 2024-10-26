package com.app.todolist.api.todos.dto;

import com.app.todolist.api.todos.controller.dto.TodoRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TodoRequestTest {

    private Validator validator = null;
    private TodoRequest todoRequest;

    @BeforeEach
    public void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        todoRequest = new TodoRequest();
    }

    @Test
    @DisplayName("title을 입력하지 않을 경우 예외가 발생한다.")
    public void titleValidTest() {
        // given
        todoRequest.setMemberId(1L);
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
    public void validSuccessTest() {
        // given
        todoRequest.setMemberId(1L);
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

    @Test
    @DisplayName("title을 최소 2자리이상 입력하지않으면 예외가 발생한다.")
    public void minTitleTest() {
        // given
        todoRequest.setMemberId(1L);
        todoRequest.setTitle("t");
        todoRequest.setContent("test content");

        // when
        Set<ConstraintViolation<TodoRequest>> violations = validator.validate(todoRequest);

        // then
        Assertions.assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("제목은 2자리 이상, 30자리 이하 입력이 가능합니다.")
                .hasSize(1);
    }

    @Test
    @DisplayName("title을 최대 30자리 초과 입력 시 예외가 발생한다.")
    public void maxTitleTest() {
        // given
        todoRequest.setMemberId(1L);
        todoRequest.setTitle("a".repeat(31));
        todoRequest.setContent("test content");

        // when
        Set<ConstraintViolation<TodoRequest>> violations = validator.validate(todoRequest);

        // then
        Assertions.assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("제목은 2자리 이상, 30자리 이하 입력이 가능합니다.")
                .hasSize(1);
    }

    @Test
    @DisplayName("title을 Size범위 내에 입력 시 예외가 발생하지 않는다.")
    public void sizeSuccessTest() {
        // given
        todoRequest.setMemberId(1L);
        todoRequest.setTitle("todo title");
        todoRequest.setContent("test content");

        // when
        Set<ConstraintViolation<TodoRequest>> violations = validator.validate(todoRequest);

        // then
        Assertions.assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains()
                .hasSize(0);
    }

}
