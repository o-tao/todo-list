package com.app.todolist.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class TodoExceptionResponse<T> {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime time;
    private final HttpStatus status;
    private final int code;
    private final T message;

    private TodoExceptionResponse(LocalDateTime time, HttpStatus status, T message) {
        this.time = time;
        this.status = status;
        this.code = status.value();
        this.message = message;
    }


    public static <T> TodoExceptionResponse<T> of(LocalDateTime time, HttpStatus status, T message) {
        return new TodoExceptionResponse<>(time, status, message);
    }

    public static <T> TodoExceptionResponse<T> badRequest(T message) {
        return of(LocalDateTime.now(), HttpStatus.BAD_REQUEST, message);
    }
}
