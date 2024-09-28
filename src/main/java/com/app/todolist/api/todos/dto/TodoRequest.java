package com.app.todolist.api.todos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TodoRequest {

    private Long memberId;
    @NotBlank(message = "제목을 입력하세요.")
    private String title;
    private String content;
}
