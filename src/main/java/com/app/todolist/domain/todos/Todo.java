package com.app.todolist.domain.todos;

import com.app.todolist.domain.BaseEntity;
import com.app.todolist.domain.members.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "todos")
public class Todo extends BaseEntity {

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false, length = 250)
    private String content;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Todo create(Member member, String title, String content) {
        Todo todo = new Todo();
        todo.title = title;
        todo.content = content;
        todo.status = Status.TODO;
        todo.member = member;
        return todo;
    }
}
