package com.app.todolist.api.todos.dto;

import com.app.todolist.api.todos.controller.dto.TodoSearchRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TodoSearchRequestTest {

    @Test
    @DisplayName("페이지가 음수 일 경우 예외가 발생한다.")
    public void pageNegativeValidationTest() {
        // given
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        TodoSearchRequest searchRequest = new TodoSearchRequest();
        searchRequest.setPage(-1);
        searchRequest.setSize(10);

        // when
        Set<ConstraintViolation<TodoSearchRequest>> violations = validator.validate(searchRequest);

        // then
        Assertions.assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("페이지는 양수여야 합니다.")
                .hasSize(1);
    }

    @Test
    @DisplayName("데이터 사이즈가 음수 일 경우 예외가 발생한다.")
    public void sizeNegativeValidationTest() {
        // given
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        TodoSearchRequest searchRequest = new TodoSearchRequest();
        searchRequest.setPage(1);
        searchRequest.setSize(-1);

        // when
        Set<ConstraintViolation<TodoSearchRequest>> violations = validator.validate(searchRequest);

        // then
        Assertions.assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("한 페이지에 조회 할 데이터 수는 양수여야 합니다.")
                .hasSize(1);
    }

}
