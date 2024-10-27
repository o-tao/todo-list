package com.app.todolist.web.exception;

import com.app.todolist.web.exception.dto.TodoExceptionResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<TodoExceptionResponse> responseBindException(BindException exception) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(
                new TodoExceptionResponse(httpStatus, Objects.requireNonNull(exception.getFieldError()).getDefaultMessage()),
                httpStatus
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<TodoExceptionResponse> requestHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        InvalidFormatException invalidFormatException = (InvalidFormatException) exception.getCause();
        String fieldName = invalidFormatException.getPath().getFirst().getFieldName();
        String targetType = invalidFormatException.getTargetType().getSimpleName();

        String errorMessage = String.format(ErrorCode.INVALID_JSON_INPUT.getMessage(), fieldName, targetType);
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(
                new TodoExceptionResponse(httpStatus, Objects.requireNonNull(errorMessage)), httpStatus);
    }

    @ExceptionHandler(TodoApplicationException.class)
    public ResponseEntity<TodoExceptionResponse> responseTodoApplicationException(TodoApplicationException exception) {
        TodoExceptionResponse errorResult = new TodoExceptionResponse(
                exception.getExceptionHttpStatus(), exception.getExceptionMessage());
        return new ResponseEntity<>(errorResult, exception.getExceptionHttpStatus());
    }
}
