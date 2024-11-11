package com.app.todolist.config.redis.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSession {
    private Long id;

    public MemberSession(Long id) {
        this.id = id;
    }
}
