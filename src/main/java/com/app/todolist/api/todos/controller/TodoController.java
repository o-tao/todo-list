package com.app.todolist.api.todos.controller;

import com.app.todolist.api.todos.controller.dto.*;
import com.app.todolist.api.todos.service.TodoService;
import com.app.todolist.domain.todos.Todo;
import com.app.todolist.web.util.PaginationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public TodoResponse createTodo(@RequestBody @Valid TodoRequest todoRequest) {
        Todo todo = todoService.createTodo(
                todoRequest.getMemberId(), todoRequest.getTitle(), todoRequest.getContent());
        return TodoResponse.of(todo);
    }

    @GetMapping
    public PaginationResponse<TodoSearchResponse> searchTodosByOptions(@Valid TodoSearchRequest searchRequest) {
        return todoService.searchTodosByOptions(searchRequest.toOption());
    }

    @PutMapping("/{id}")
    public TodoUpdateResponse updateTodo(@PathVariable Long id,
                                         @RequestBody @Valid TodoUpdateRequest todoUpdateRequest) {
        Todo updatedTodo = todoService.updateTodo(id, todoUpdateRequest.toEntity());
        return TodoUpdateResponse.of(updatedTodo);
    }
}
