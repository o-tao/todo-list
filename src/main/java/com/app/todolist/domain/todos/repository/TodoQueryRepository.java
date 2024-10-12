package com.app.todolist.domain.todos.repository;

import com.app.todolist.api.todos.dto.TodosWithOptions;
import com.app.todolist.domain.todos.Todo;
import com.app.todolist.domain.todos.TodoStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.app.todolist.domain.todos.QTodo.todo;

@Repository
@RequiredArgsConstructor
public class TodoQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<Todo> findByTitleContains(TodosWithOptions todosWithOptions, Pageable pageable) {
        long total = jpaQueryFactory
                .selectFrom(todo)
                .where(
                        todo.member.id.eq(todosWithOptions.getMemberId())
                                .and(titleCondition(todosWithOptions.getTitle()))
                                .and(statusCondition(todosWithOptions.getStatus()))
                )
                .fetch().size();

        List<Todo> results = jpaQueryFactory
                .selectFrom(todo)
                .where(
                        todo.member.id.eq(todosWithOptions.getMemberId())
                                .and(titleCondition(todosWithOptions.getTitle()))
                                .and(statusCondition(todosWithOptions.getStatus()))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression titleCondition(String title) {
        return title != null ? todo.title.contains(title) : null;
    }

    private BooleanExpression statusCondition(TodoStatus status) {
        return status != null ? todo.status.eq(status) : null;
    }
}
