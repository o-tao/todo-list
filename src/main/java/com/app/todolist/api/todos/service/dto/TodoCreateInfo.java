package com.app.todolist.api.todos.service.dto;

import lombok.Getter;

@Getter
public class TodoCreateInfo {

    private final String title;
    private final String content;

    public TodoCreateInfo(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
