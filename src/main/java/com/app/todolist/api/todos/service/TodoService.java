package com.app.todolist.api.todos.service;

import com.app.todolist.api.members.service.MemberService;
import com.app.todolist.api.todos.controller.dto.TodoSearchResponse;
import com.app.todolist.api.todos.service.dto.TodoCreateInfo;
import com.app.todolist.api.todos.service.dto.TodoUpdateInfo;
import com.app.todolist.api.todos.service.dto.TodosWithOptions;
import com.app.todolist.domain.members.Member;
import com.app.todolist.domain.todos.Todo;
import com.app.todolist.domain.todos.TodoStatus;
import com.app.todolist.domain.todos.repository.TodoQueryRepository;
import com.app.todolist.domain.todos.repository.TodoRepository;
import com.app.todolist.web.exception.ErrorCode;
import com.app.todolist.web.exception.TodoApplicationException;
import com.app.todolist.web.util.PaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;
    private final TodoQueryRepository todoQueryRepository;

    private final MemberService memberService;


    @Transactional
    public Todo createTodo(TodoCreateInfo todoCreateInfo, Long memberId) {
        Member member = memberService.findMemberById(memberId);
        return todoRepository.save(Todo.create(member, todoCreateInfo.getTitle(), todoCreateInfo.getContent()));
    }

    @Transactional
    public Todo updateTodo(Long todoId, TodoUpdateInfo todoUpdateInfo, Long memberId) {
        Todo todo = findTodoById(todoId);
        validateTodoOwnership(todo, memberId);
        todo.update(todoUpdateInfo.getTitle(), todoUpdateInfo.getContent());
        return todo;
    }

    @Transactional
    public Todo updateTodoStatus(Long todoId, TodoStatus todoStatus, Long memberId) {
        Todo todo = findTodoById(todoId);
        validateTodoOwnership(todo, memberId);
        todo.updateStatus(todoStatus);
        return todo;
    }

    public PaginationResponse<TodoSearchResponse> searchTodosByOptions(TodosWithOptions todosWithOptions) {
        memberService.findMemberById(todosWithOptions.getMemberId());
        List<Todo> todos = todoQueryRepository.findTodosByOptions(todosWithOptions);
        long totalElements = todoQueryRepository.countByTodo(todosWithOptions);
        return PaginationResponse.of(
                todos.stream().map(TodoSearchResponse::of).toList(),
                todosWithOptions.getPage(),
                todosWithOptions.getSize(),
                totalElements
        );
    }

    public Todo getTodoDetails(Long todoId, Long memberId) {
        Todo todo = findTodoById(todoId);
        validateTodoOwnership(todo, memberId);
        return todo;
    }

    private Todo findTodoById(Long id) {
        return todoRepository.findById(id).orElseThrow(()
                -> new TodoApplicationException(ErrorCode.TODO_NOT_FOUND)
        );
    }

    private void validateTodoOwnership(Todo todo, Long memberId) {
        if (!todo.getMember().getId().equals(memberId)) {
            throw new TodoApplicationException(ErrorCode.PERMISSION_DENIED);
        }
    }
}
