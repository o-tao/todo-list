package com.app.todolist.config.redis.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSession {
    private Long memberId;

    public MemberSession(Long memberId) {
        this.memberId = memberId;
    }
}
