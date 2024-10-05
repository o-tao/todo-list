package com.app.todolist.api.todos;

import com.app.todolist.domain.members.Member;
import com.app.todolist.domain.members.repository.MemberRepository;
import com.app.todolist.domain.todos.Todo;
import com.app.todolist.domain.todos.TodoStatus;
import com.app.todolist.domain.todos.repository.TodoCustomRepository;
import com.app.todolist.domain.todos.repository.TodoRepository;
import com.app.todolist.exception.ErrorCode;
import com.app.todolist.exception.TodoApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;
    private final TodoCustomRepository todoCustomRepository;

    public Todo createTodo(Long memberId, String title, String content) {
        Member member = memberRepository.findById(memberId).orElseThrow(()
                -> new TodoApplicationException(ErrorCode.MEMBER_NOT_FOUND));
        return todoRepository.save(Todo.create(member, title, content));
    }

    public List<Todo> searchTodosByTitle(Long memberId, String title, TodoStatus status) {
        memberRepository.findById(memberId).orElseThrow(()
                -> new TodoApplicationException(ErrorCode.MEMBER_NOT_FOUND));
        return todoCustomRepository.findByTitleContains(memberId, title, status);
    }
}
