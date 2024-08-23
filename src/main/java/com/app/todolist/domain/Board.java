package com.app.todolist.domain;

import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class Board extends BaseEntity {

    private String title;
    private String content;

}
