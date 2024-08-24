package com.app.todolist.domain.board;

import com.app.todolist.domain.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Board extends BaseEntity {

    private String title;
    private String content;

    private Board(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static Board create(String title, String content) {
        return new Board(title, content);
    }
}
