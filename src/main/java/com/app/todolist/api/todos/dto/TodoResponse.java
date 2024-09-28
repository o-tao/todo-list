package com.app.todolist.api.todos.dto;

import com.app.todolist.domain.todos.Todo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class TodoResponse {

    private Long id;
    private String title;
    private String content;
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    private TodoMemberInfo member;

    public static TodoResponse of(Todo todo) {
        return TodoResponse.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .content(todo.getContent())
                .status(todo.getStatus().getType())
                .createdAt(todo.getCreatedAt())
                .updatedAt(todo.getUpdatedAt())
                .member(new TodoMemberInfo(todo.getMember().getId(), todo.getMember().getEmail()))
                .build();
    }

    @Getter
    public static class TodoMemberInfo {
        private final Long id;
        private final String email;

        public TodoMemberInfo(Long id, String email) {
            this.id = id;
            this.email = email;
        }
    }
}
