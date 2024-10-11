package com.app.todolist.api.todos;

import com.app.todolist.api.todos.dto.TodosWithOptions;
import com.app.todolist.domain.members.Member;
import com.app.todolist.domain.members.repository.MemberRepository;
import com.app.todolist.domain.todos.Todo;
import com.app.todolist.domain.todos.repository.TodoQueryRepository;
import com.app.todolist.domain.todos.repository.TodoRepository;
import com.app.todolist.exception.ErrorCode;
import com.app.todolist.exception.TodoApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;
    private final TodoQueryRepository todoQueryRepository;

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(()
                -> new TodoApplicationException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional
    public Todo createTodo(Long memberId, String title, String content) {
        Member member = findMemberById(memberId);
        return todoRepository.save(Todo.create(member, title, content));
    }

    public List<Todo> searchTodosByOptions(TodosWithOptions todosWithOptions) {
        findMemberById(todosWithOptions.getMemberId());
        return todoQueryRepository.findByTitleContains(todosWithOptions);
    }
}
