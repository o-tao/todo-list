package com.app.todolist.api.todos.controller.dto;

import com.app.todolist.domain.todos.Todo;
import com.app.todolist.domain.todos.TodoStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TodoUpdateResponse {

    private Long id;
    private String title;
    private String content;
    private TodoStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public static TodoUpdateResponse of(Todo todo) {
        TodoUpdateResponse todoUpdateResponse = new TodoUpdateResponse();
        todoUpdateResponse.id = todo.getId();
        todoUpdateResponse.title = todo.getTitle();
        todoUpdateResponse.content = todo.getContent();
        todoUpdateResponse.status = todo.getStatus();
        todoUpdateResponse.createdAt = todo.getCreatedAt();
        todoUpdateResponse.updatedAt = todo.getUpdatedAt();
        return todoUpdateResponse;
    }
}
