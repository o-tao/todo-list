package com.app.todolist.domain.todos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TodoStatus {
    TODO("할일");

    private final String type;
}
