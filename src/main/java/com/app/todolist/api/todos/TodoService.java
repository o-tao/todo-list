package com.app.todolist.api.todos;

import com.app.todolist.domain.members.Member;
import com.app.todolist.domain.members.MemberRepository;
import com.app.todolist.domain.todos.Todo;
import com.app.todolist.domain.todos.TodoRepository;
import com.app.todolist.exception.ErrorCode;
import com.app.todolist.exception.TodoApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;

    private final MemberRepository memberRepository;

    public Todo createTodo(Long memberId, String title, String content) {
        Member member = memberRepository.findById(memberId).orElseThrow(()
                -> new TodoApplicationException(ErrorCode.NOT_EXISTED_EMAIL));
        return todoRepository.save(Todo.create(member, title, content));
    }
}
