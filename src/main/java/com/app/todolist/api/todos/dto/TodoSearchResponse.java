package com.app.todolist.api.todos.dto;

import com.app.todolist.domain.todos.Todo;
import com.app.todolist.domain.todos.TodoStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class TodoSearchResponse {

    private List<TodoSearchResultResponse> contents;
    private long totalElements;
    private int totalPages;

    public static TodoSearchResponse of(Page<Todo> todos) {
        List<TodoSearchResultResponse> list = todos.stream().map(TodoSearchResultResponse::of).toList();
        return TodoSearchResponse.builder()
                .contents(list)
                .totalElements(todos.getTotalElements())
                .totalPages(todos.getTotalPages())
                .build();
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class TodoSearchResultResponse {
        private String title;
        private TodoStatus status;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime updatedAt;


        public static TodoSearchResultResponse of(Todo todo) {
            return new TodoSearchResultResponse(
                    todo.getTitle(), todo.getStatus(), todo.getCreatedAt(), todo.getUpdatedAt());
        }
    }
}
