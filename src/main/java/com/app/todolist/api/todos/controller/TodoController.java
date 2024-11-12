package com.app.todolist.api.todos.controller;

import com.app.todolist.api.todos.controller.dto.*;
import com.app.todolist.api.todos.service.TodoService;
import com.app.todolist.config.auth.checkAuth.CheckAuth;
import com.app.todolist.config.auth.loginMember.LoginMember;
import com.app.todolist.config.redis.dto.MemberSession;
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

    @CheckAuth
    @PostMapping
    public TodoResponse createTodo(@RequestBody @Valid TodoRequest todoRequest,
                                   @LoginMember MemberSession memberSession) {
        Todo todo = todoService.createTodo(
                memberSession.getMemberId(), todoRequest.getTitle(), todoRequest.getContent());
        return TodoResponse.of(todo);
    }

    @CheckAuth
    @GetMapping
    public PaginationResponse<TodoSearchResponse> searchTodosByOptions(@Valid TodoSearchRequest searchRequest,
                                                                       @LoginMember MemberSession memberSession
    ) {
        return todoService.searchTodosByOptions(searchRequest.toOption(memberSession.getMemberId()));
    }

    @GetMapping("/{id}")
    public TodoResponse todoDetails(@PathVariable Long id) {
        Todo todo = todoService.findTodoById(id);
        return TodoResponse.of(todo);
    }

    @PutMapping("/{id}")
    public TodoResponse updateTodo(@PathVariable Long id,
                                   @RequestBody @Valid TodoUpdateRequest todoUpdateRequest) {
        Todo todo = todoService.updateTodo(id, todoUpdateRequest.toUpdate());
        return TodoResponse.of(todo);
    }

    @PutMapping("/{id}/status")
    public TodoResponse updateTodoStatus(@PathVariable Long id,
                                         @RequestBody @Valid TodoStatusUpdateRequest todoStatusUpdateRequest) {
        Todo todo = todoService.updateTodoStatus(id, todoStatusUpdateRequest.getStatus());
        return TodoResponse.of(todo);
    }
}
