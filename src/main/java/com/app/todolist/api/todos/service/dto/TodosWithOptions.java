package com.app.todolist.api.todos.service.dto;

import com.app.todolist.domain.todos.TodoStatus;
import lombok.Getter;

@Getter
public class TodosWithOptions {

    private final Long memberId;
    private final String title;
    private final TodoStatus status;
    private final long page;
    private final long size;

    public TodosWithOptions(Long memberId, String title, TodoStatus status, long page, long size) {
        this.memberId = memberId;
        this.title = title;
        this.status = status;
        this.page = page;
        this.size = size;
    }
}
