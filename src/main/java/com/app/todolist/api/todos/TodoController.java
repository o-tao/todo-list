package com.app.todolist.api.todos;

import com.app.todolist.api.todos.dto.*;
import com.app.todolist.domain.todos.Todo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public TodoSearchResponse searchTodosByTitle(@Valid TodoSearchRequest searchRequest) {
        List<Todo> todos = todoService.searchTodosWithOptions(
                new TodosWithOptions(searchRequest.getMemberId(), searchRequest.getTitle(), searchRequest.getStatus()));
        return TodoSearchResponse.of(todos);
    }
}
