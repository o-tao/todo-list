package com.app.todolist.web.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    DUPLICATED_MEMBER_ID(HttpStatus.CONFLICT, "이미 가입된 이메일이 존재합니다."),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "회원이 존재하지 않습니다."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호를 확인하세요."),
    TODO_NOT_FOUND(HttpStatus.BAD_REQUEST, "TODO가 존재하지 않습니다."),
    INVALID_JSON_INPUT(HttpStatus.BAD_REQUEST, "%s 필드 값이 잘못되었습니다. [%s] 타입이어야 합니다."),
    LOGIN_FORBIDDEN(HttpStatus.FORBIDDEN, "로그인 후 이용해주세요."),
    PERMISSION_DENIED(HttpStatus.FORBIDDEN, "이 작업을 수행할 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
