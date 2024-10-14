package com.app.todolist.web.exception;

import com.app.todolist.web.exception.dto.TodoExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<TodoExceptionResponse> responseMethodArgumentNotValidException(BindException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        String message = fieldErrors.stream().map(FieldError::getField).toList().toString();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        String errorMessage = message + " 필드 값이 잘못되었습니다.";
        return new ResponseEntity<>(
                new TodoExceptionResponse(httpStatus, Objects.requireNonNull(errorMessage)), httpStatus);
    }

    @ExceptionHandler(TodoApplicationException.class)
    public ResponseEntity<TodoExceptionResponse> responseApplicationException(TodoApplicationException exception) {
        TodoExceptionResponse errorResult = new TodoExceptionResponse(
                exception.getExceptionHttpStatus(), exception.getExceptionMessage());
        return new ResponseEntity<>(errorResult, exception.getExceptionHttpStatus());
    }
}
