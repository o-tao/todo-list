package com.app.todolist.domain.todos.repository;

import com.app.todolist.domain.todos.Todo;
import com.app.todolist.domain.todos.TodoStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.app.todolist.domain.todos.QTodo.todo;

@Repository
@RequiredArgsConstructor
public class TodoCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Todo> findByTitleContains(Long memberId, String title, TodoStatus status) {
        return jpaQueryFactory
                .selectFrom(todo)
                .where(
                        todo.member.id.eq(memberId)
                                .and(titleCondition(title))
                                .and(statusCondition(status))
                )
                .fetch();
    }

    private BooleanExpression titleCondition(String title) {
        return title != null ? todo.title.contains(title) : null;
    }

    private BooleanExpression statusCondition(TodoStatus status) {
        return status != null ? todo.status.eq(status) : null;
    }
}
