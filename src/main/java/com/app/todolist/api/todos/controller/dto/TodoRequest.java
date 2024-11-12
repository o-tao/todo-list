package com.app.todolist.api.todos.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TodoRequest {

    @Size(min = 2, max = 30, message = "제목은 2자리 이상, 30자리 이하 입력이 가능합니다.")
    @NotBlank(message = "제목을 입력하세요.")
    private String title;

    @Size(max = 250, message = "내용은 250자리 이하 입력이 가능합니다.")
    @NotNull(message = "내용을 입력하세요.")
    private String content;
}
