package com.app.todolist.domain.todos;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TodosRepository extends JpaRepository<Todo, Long> {

}
