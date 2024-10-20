package com.app.todolist.api.todos.controller.dto;

import com.app.todolist.api.todos.service.dto.TodosWithOptions;
import com.app.todolist.domain.todos.TodoStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TodoSearchRequest {

    @NotNull(message = "회원 ID를 입력하세요.")
    @Positive(message = "회원 ID는 양수여야 합니다.")
    private Long memberId;

    private String title;
    private TodoStatus status;

    @Positive(message = "페이지는 양수여야 합니다.")
    private long page = 1;

    @Positive(message = "한 페이지에 조회 할 데이터 수는 양수여야 합니다.")
    private long size = 10;

    public TodosWithOptions toOption() {
        return new TodosWithOptions(memberId, title, status, page, size);
    }
}