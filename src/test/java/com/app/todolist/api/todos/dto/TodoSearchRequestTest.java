package com.app.todolist.api.todos.dto;

import com.app.todolist.domain.todos.TodoStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TodoSearchRequestTest {

    @Test
    @DisplayName("memberId가 null일 경우 예외가 발생한다.")
    public void memberIdNotNullValidTest() {
        // given
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        TodoSearchRequest searchRequest = new TodoSearchRequest();
        searchRequest.setTitle("title");
        searchRequest.setStatus(TodoStatus.TODO);
        searchRequest.setMemberId(null);

        // when
        Set<ConstraintViolation<TodoSearchRequest>> violations = validator.validate(searchRequest);

        // then
        Assertions.assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("회원 ID를 입력하세요.")
                .hasSize(1);
    }

    @Test
    @DisplayName("memberId가 음수일 경우 예외가 발생한다.")
    public void memberIdPositiveValidTest() {
        // given
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        TodoSearchRequest searchRequest = new TodoSearchRequest();
        searchRequest.setTitle("title");
        searchRequest.setStatus(TodoStatus.TODO);
        searchRequest.setMemberId(-1L);

        // when
        Set<ConstraintViolation<TodoSearchRequest>> violations = validator.validate(searchRequest);

        // then
        Assertions.assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("회원 ID는 양수여야 합니다.")
                .hasSize(1);
    }

    @Test
    @DisplayName("memberId의 값이 정상일 경우 예외가 발생하지 않는다.")
    public void memberIdValidSuccessTest() {
        // given
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        TodoSearchRequest searchRequest = new TodoSearchRequest();
        searchRequest.setTitle("title");
        searchRequest.setStatus(TodoStatus.TODO);
        searchRequest.setMemberId(1L);

        // when
        Set<ConstraintViolation<TodoSearchRequest>> violations = validator.validate(searchRequest);

        // then
        Assertions.assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains()
                .hasSize(0);
    }

}
