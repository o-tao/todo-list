package com.app.todolist.api.todos.service;

import com.app.todolist.api.members.MemberService;
import com.app.todolist.api.todos.controller.dto.TodoSearchResponse;
import com.app.todolist.api.todos.service.dto.TodosWithOptions;
import com.app.todolist.domain.members.Member;
import com.app.todolist.domain.todos.Todo;
import com.app.todolist.domain.todos.repository.TodoQueryRepository;
import com.app.todolist.domain.todos.repository.TodoRepository;
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
    public Todo createTodo(Long memberId, String title, String content) {
        Member member = memberService.findMemberById(memberId);
        return todoRepository.save(Todo.create(member, title, content));
    }

    public PaginationResponse<TodoSearchResponse> searchTodosByOptions(TodosWithOptions todosWithOptions) {
        memberService.findMemberById(todosWithOptions.getMemberId());
        List<Todo> todos = todoQueryRepository.findTodosByOptions(todosWithOptions);
        long totalElements = todoQueryRepository.countByTodo(todosWithOptions);
        return PaginationResponse.of(
                todos.stream().map(TodoSearchResponse::of).toList(),
                todosWithOptions.getSize(),
                totalElements
        );
    }
}