package com.app.todolist.domain.todos.repository;

import com.app.todolist.api.todos.service.dto.TodosWithOptions;
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
public class TodoQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Todo> findTodosByOptions(TodosWithOptions todosWithOptions) {
        return jpaQueryFactory
                .selectFrom(todo)
                .where(
                        todo.member.id.eq(todosWithOptions.getMemberId())
                                .and(containsTitle(todosWithOptions.getTitle()))
                                .and(eqStatus(todosWithOptions.getStatus()))
                )
                .offset((todosWithOptions.getPage() - 1) * todosWithOptions.getSize())
                .limit(todosWithOptions.getSize())
                .fetch();
    }

    public Long countByTodo(TodosWithOptions todosWithOptions) {
        return jpaQueryFactory
                .select(todo.count())
                .from(todo)
                .where(
                        todo.member.id.eq(todosWithOptions.getMemberId())
                                .and(containsTitle(todosWithOptions.getTitle()))
                                .and(eqStatus(todosWithOptions.getStatus()))
                )
                .fetchOne();
    }

    private BooleanExpression containsTitle(String title) {
        return title != null ? todo.title.contains(title) : null;
    }

    private BooleanExpression eqStatus(TodoStatus status) {
        return status != null ? todo.status.eq(status) : null;
    }
}
