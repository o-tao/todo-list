package com.app.todolist.api.members.controller.dto.request;

import com.app.todolist.api.members.service.dto.MemberLoginInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberLoginRequest {

    @NotBlank(message = "이메일을 입력하세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;

    public MemberLoginInfo toLogin() {
        return new MemberLoginInfo(email, password);
    }
}
