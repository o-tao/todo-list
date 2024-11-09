package com.app.todolist.config.redis.dto;

import lombok.Getter;

@Getter
public class MemberSession {
    private final Long id;

    public MemberSession(Long id) {
        this.id = id;
    }
}
