package com.app.todolist.domain.members;

import com.app.todolist.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "members")
public class Member extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 60)
    private String password;

    public static Member create(String email, String password) {
        Member member = new Member();
        member.email = email;
        member.password = password;
        return member;
    }
}
