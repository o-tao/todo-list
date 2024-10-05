package com.app.todolist.domain.todos.repository;

import com.app.todolist.domain.todos.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
