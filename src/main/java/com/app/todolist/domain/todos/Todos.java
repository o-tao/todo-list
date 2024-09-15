package com.app.todolist.domain.todos;

import com.app.todolist.domain.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Todos extends BaseEntity {

    private String title;
    private String content;

    private Todos(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static Todos create(String title, String content) {
        return new Todos(title, content);
    }
}
