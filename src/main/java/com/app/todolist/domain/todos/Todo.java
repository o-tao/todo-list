package com.app.todolist.domain.todos;

import com.app.todolist.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "todos")
public class Todo extends BaseEntity {

    @Column(nullable = false, length = 30)
    private String title;

    private String content;

    public static Todo save(String title, String content) {
        Todo todo = new Todo();
        todo.title = title;
        todo.content = content;
        return todo;
    }
}
