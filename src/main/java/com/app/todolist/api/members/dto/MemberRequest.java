package com.app.todolist.api.members.dto;

import com.app.todolist.domain.members.Member;
import lombok.Getter;

@Getter
public class MemberRequest {

    private String email;
    private String password;

    public Member toEntity() {
        return Member.create(email, password);
    }
}
