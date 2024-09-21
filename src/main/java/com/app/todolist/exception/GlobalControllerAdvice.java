package com.app.todolist.exception;

import com.app.todolist.api.dto.TodoExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<TodoExceptionResponse> responseMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> error = new HashMap<>();
        exception.getAllErrors().forEach(
                c -> error.put(((FieldError) c).getField(), c.getDefaultMessage()));
        return new ResponseEntity<>(TodoExceptionResponse.badRequest(error), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<TodoExceptionResponse> responseApplicationException(ApplicationException exception) {
        TodoExceptionResponse<String> errorResult = TodoExceptionResponse.of(
                LocalDateTime.now(), exception.getExceptionHttpStatus(), exception.getExceptionMessage());
        return new ResponseEntity<>(errorResult, exception.getExceptionHttpStatus());
    }
}
