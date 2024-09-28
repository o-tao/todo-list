package com.app.todolist.api.todos;

import com.app.todolist.api.todos.dto.TodoRequest;
import com.app.todolist.api.todos.dto.TodoResponse;
import com.app.todolist.domain.todos.Todo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public TodoResponse create(@RequestBody @Valid TodoRequest todoRequest) {
        Todo todo = todoService.createTodo(
                todoRequest.getMemberId(), todoRequest.getTitle(), todoRequest.getContent()
        );
        return TodoResponse.of(todo);
    }
}
