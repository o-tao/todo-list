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
                todoRequest.toCreate(), memberSession.getMemberId());
        return TodoResponse.of(todo);
    }

    @CheckAuth
    @GetMapping
    public PaginationResponse<TodoSearchResponse> searchTodosByOptions(@Valid TodoSearchRequest searchRequest,
                                                                       @LoginMember MemberSession memberSession) {
        return todoService.searchTodosByOptions(searchRequest.toOption(memberSession.getMemberId()));
    }

    @CheckAuth
    @GetMapping("/{todoId}")
    public TodoResponse todoDetails(@PathVariable Long todoId,
                                    @LoginMember MemberSession memberSession) {
        Todo todo = todoService.getTodoDetails(todoId, memberSession.getMemberId());
        return TodoResponse.of(todo);
    }

    @CheckAuth
    @PutMapping("/{todoId}")
    public TodoResponse updateTodo(@PathVariable Long todoId,
                                   @RequestBody @Valid TodoUpdateRequest todoUpdateRequest,
                                   @LoginMember MemberSession memberSession) {
        Todo todo = todoService.updateTodo(todoId, todoUpdateRequest.toUpdate(), memberSession.getMemberId());
        return TodoResponse.of(todo);
    }

    @CheckAuth
    @PutMapping("/{todoId}/status")
    public TodoResponse updateTodoStatus(@PathVariable Long todoId,
                                         @RequestBody @Valid TodoStatusUpdateRequest todoStatusUpdateRequest,
                                         @LoginMember MemberSession memberSession) {
        Todo todo = todoService.updateTodoStatus(todoId, todoStatusUpdateRequest.getStatus(), memberSession.getMemberId());
        return TodoResponse.of(todo);
    }
}
