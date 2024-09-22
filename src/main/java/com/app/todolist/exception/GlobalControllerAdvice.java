package com.app.todolist.exception;

import com.app.todolist.api.dto.TodoExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<TodoExceptionResponse> responseMethodArgumentNotValidException(BindException exception) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(
                new TodoExceptionResponse(httpStatus, Objects.requireNonNull(exception.getFieldError()).getDefaultMessage()),
                httpStatus
        );
    }

    @ExceptionHandler(TodoApplicationException.class)
    public ResponseEntity<TodoExceptionResponse> responseApplicationException(TodoApplicationException exception) {
        TodoExceptionResponse errorResult = new TodoExceptionResponse(
                exception.getExceptionHttpStatus(), exception.getExceptionMessage());
        return new ResponseEntity<>(errorResult, exception.getExceptionHttpStatus());
    }
}
