package com.app.todolist.api.todos.controller.dto;

import com.app.todolist.domain.todos.TodoStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TodoStatusUpdateRequestTest {

    @Test
    @DisplayName("status가 null일 경우 예외가 발생한다.")
    public void statusNotNullValidTest() {
        // given
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        TodoStatusUpdateRequest todoStatusUpdateRequest = new TodoStatusUpdateRequest();
        todoStatusUpdateRequest.setStatus(null);

        // when
        Set<ConstraintViolation<TodoStatusUpdateRequest>> violations = validator.validate(todoStatusUpdateRequest);

        // then
        Assertions.assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("널이어서는 안됩니다")
                .hasSize(1);
    }

    @Test
    @DisplayName("status를 정상적으로 입력할 경우 예외가 발생하지 않는다.")
    public void statusValidSuccessTest() {
        // given
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        TodoStatusUpdateRequest todoStatusUpdateRequest = new TodoStatusUpdateRequest();
        todoStatusUpdateRequest.setStatus(TodoStatus.TODO);

        // when
        Set<ConstraintViolation<TodoStatusUpdateRequest>> violations = validator.validate(todoStatusUpdateRequest);

        // then
        Assertions.assertThat(violations).isEmpty();
    }

}
