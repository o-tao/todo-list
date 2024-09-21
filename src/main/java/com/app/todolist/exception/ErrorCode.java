package com.app.todolist.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    DUPLICATED_MEMBER_ID(HttpStatus.CONFLICT, "이미 가입된 이메일이 존재합니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
