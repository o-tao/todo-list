package com.app.todolist.api.todos.dto;

import com.app.todolist.domain.todos.TodoStatus;
import lombok.Getter;

@Getter
public class TodosWithOptions {

    private final Long memberId;
    private final String title;
    private final TodoStatus status;

    public TodosWithOptions(Long memberId, String title, TodoStatus status) {
        this.memberId = memberId;
        this.title = title;
        this.status = status;
    }
}
