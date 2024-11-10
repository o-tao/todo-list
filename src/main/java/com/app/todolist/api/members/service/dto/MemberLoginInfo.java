package com.app.todolist.api.members.service.dto;

import lombok.Getter;

@Getter
public class MemberLoginInfo {

    private final String email;
    private final String password;

    public MemberLoginInfo(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
