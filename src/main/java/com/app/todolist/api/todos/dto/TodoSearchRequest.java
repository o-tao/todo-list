package com.app.todolist.api.todos.dto;

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

    public TodosWithOptions toOption() {
        return new TodosWithOptions(memberId, title, status);
    }
}
