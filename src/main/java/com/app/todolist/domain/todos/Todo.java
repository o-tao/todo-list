package com.app.todolist.domain.todos;

import com.app.todolist.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "todos")
public class Todo extends BaseEntity {

    private String title;
    private String content;

    private Todo(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static Todo create(String title, String content) {
        return new Todo(title, content);
    }
}
