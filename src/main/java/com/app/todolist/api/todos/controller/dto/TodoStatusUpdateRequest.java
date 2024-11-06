package com.app.todolist.api.todos.controller.dto;

import com.app.todolist.domain.todos.TodoStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoStatusUpdateRequest {

    @NotNull
    private TodoStatus status;
}
