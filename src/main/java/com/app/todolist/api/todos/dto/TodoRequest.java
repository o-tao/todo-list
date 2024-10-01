package com.app.todolist.api.todos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TodoRequest {

    private Long memberId;

    @Size(min = 2, max = 30, message = "제목은 2자리 이상, 30자리 이하 입력이 가능합니다.")
    @NotBlank(message = "제목을 입력하세요.")
    private String title;

    private String content;
}
