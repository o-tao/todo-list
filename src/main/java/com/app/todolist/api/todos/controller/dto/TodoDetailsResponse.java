package com.app.todolist.api.todos.controller.dto;

import com.app.todolist.domain.todos.Todo;
import com.app.todolist.domain.todos.TodoStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TodoDetailsResponse {

    private String title;
    private String content;
    private TodoStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public static TodoDetailsResponse of(Todo todo) {
        TodoDetailsResponse todoDetailsResponse = new TodoDetailsResponse();
        todoDetailsResponse.title = todo.getTitle();
        todoDetailsResponse.content = todo.getContent();
        todoDetailsResponse.status = todo.getStatus();
        todoDetailsResponse.createdAt = todo.getCreatedAt();
        todoDetailsResponse.updatedAt = todo.getUpdatedAt();
        return todoDetailsResponse;
    }
}
