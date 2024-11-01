package com.app.todolist.api.todos.service.dto;

import lombok.Getter;

@Getter
public class TodoUpdateInfo {

    private final String title;
    private final String content;

    public TodoUpdateInfo(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
