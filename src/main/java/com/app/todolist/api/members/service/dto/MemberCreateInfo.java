package com.app.todolist.api.members.service.dto;

import lombok.Getter;

@Getter
public class MemberCreateInfo {

    private final String email;
    private final String password;

    public MemberCreateInfo(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
