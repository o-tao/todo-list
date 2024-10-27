package com.app.todolist.web.exception;

import com.app.todolist.web.exception.dto.TodoExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class GlobalExceptionHandlerTest {

    @Test
    @DisplayName("잘못된 값 요청 시 GlobalExceptionHandler가 BindException을 처리한다.")
    public void responseBindExceptionTest() {
        // given
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

        Object target = new Object();
        String objectName = "BindException";
        BindingResult bindingResult = new BeanPropertyBindingResult(target, objectName);

        String fieldName = "todo";
        String rejectedValue = "invalid value";
        String errorMessage = "Value is invalid";
        bindingResult.addError(new FieldError(objectName, fieldName, rejectedValue, false, null, null, errorMessage));

        BindException bindException = new BindException(bindingResult);

        // when
        ResponseEntity<TodoExceptionResponse> response = globalExceptionHandler.responseBindException(bindException);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).contains(errorMessage);

        FieldError fieldError = bindException.getFieldError();
        assertThat(fieldError).isNotNull();
        assertThat(fieldError.getField()).isEqualTo(fieldName);
        assertThat(fieldError.getRejectedValue()).isEqualTo(rejectedValue);
        assertThat(fieldError.getDefaultMessage()).isEqualTo(errorMessage);
    }

    @Test
    @DisplayName("잘못된 JSON 입력 시 GlobalExceptionHandler가 HttpMessageNotReadableException을 처리한다")
    public void requestHttpMessageNotReadableExceptionTest() throws Exception {
        // given
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        ObjectMapper objectMapper = new ObjectMapper();

        String invalidJson = "{ \"id\": \"a\", \"todo\": \"123\" }";

        HttpMessageNotReadableException exception = null;
        try {
            objectMapper.readValue(invalidJson, ExampleRequest.class);
        } catch (InvalidFormatException invalidFormatException) {
            exception = new HttpMessageNotReadableException("Invalid JSON format", invalidFormatException);
        }

        String expectedFieldName = "id";
        String expectedTargetType = "Long";
        String expectedMessage = String.format(
                ErrorCode.INVALID_JSON_INPUT.getMessage(), expectedFieldName, expectedTargetType
        );

        // when
        ResponseEntity<TodoExceptionResponse> response = globalExceptionHandler
                .requestHttpMessageNotReadableException(Objects.requireNonNull(exception));

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getBody().getMessage()).isEqualTo(expectedMessage);
    }

    @Test
    @DisplayName("커스텀 예외 TodoApplicationException 발생 시 지정한 ErrorCode를 응답한다.")
    public void responseTodoApplicationExceptionTest() {
        // given
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

        HttpStatus exceptionStatus = ErrorCode.DUPLICATED_MEMBER_ID.getHttpStatus();
        String exceptionMessage = ErrorCode.DUPLICATED_MEMBER_ID.getMessage();

        TodoApplicationException exception = new TodoApplicationException(ErrorCode.DUPLICATED_MEMBER_ID);

        // when
        ResponseEntity<TodoExceptionResponse> response = globalExceptionHandler
                .responseTodoApplicationException(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(exceptionStatus);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(exceptionStatus.value());
        assertThat(response.getBody().getMessage()).isEqualTo(exceptionMessage);
    }
}

class ExampleRequest {
    public Long id;
    public String todo;
}
