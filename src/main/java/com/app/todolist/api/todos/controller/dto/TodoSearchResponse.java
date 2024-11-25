package com.app.todolist.api.todos.controller.dto;

import com.app.todolist.domain.todos.Todo;
import com.app.todolist.domain.todos.TodoStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class TodoSearchResponse {

    Long memberId;
    String title;
    TodoStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updatedAt;

    public static TodoSearchResponse of(Todo todo) {
        return TodoSearchResponse.builder()
                .memberId(todo.getMember().getId())
                .title(todo.getTitle())
                .status(todo.getStatus())
                .createdAt(todo.getCreatedAt())
                .updatedAt(todo.getUpdatedAt())
                .build();
    }
}
