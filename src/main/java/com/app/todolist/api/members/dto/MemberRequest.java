package com.app.todolist.api.members.dto;

import com.app.todolist.domain.members.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class MemberRequest {

    @NotBlank(message = "이메일을 입력하세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;

    public Member toEntity() {
        return Member.create(email, password);
    }
}
