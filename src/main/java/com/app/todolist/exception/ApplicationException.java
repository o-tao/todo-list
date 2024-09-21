package com.app.todolist.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ApplicationException extends RuntimeException {

    private final ErrorCode errorCode;

    public String getExceptionMessage() {
        return errorCode.getMessage();
    }

    public HttpStatus getExceptionHttpStatus() {
        return errorCode.getHttpStatus();
    }
}
