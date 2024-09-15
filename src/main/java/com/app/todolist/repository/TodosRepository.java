package com.app.todolist.repository;

import com.app.todolist.domain.todos.Todos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodosRepository extends JpaRepository<Todos, Long> {

}
