package com.app.todolist.api.todos;

import com.app.todolist.api.todos.dto.TodoRequest;
import com.app.todolist.api.todos.dto.TodoResponse;
import com.app.todolist.api.todos.dto.TodoSearchRequest;
import com.app.todolist.api.todos.dto.TodoSearchResponse;
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
    public TodoSearchResponse searchTodosByOptions(@Valid TodoSearchRequest searchRequest) {
        List<Todo> todos = todoService.searchTodosByOptions(searchRequest.toOption());
        return TodoSearchResponse.of(todos);
    }
}
