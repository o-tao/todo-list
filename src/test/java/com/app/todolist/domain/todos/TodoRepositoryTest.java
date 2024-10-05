package com.app.todolist.domain.todos;

import com.app.todolist.domain.members.Member;
import com.app.todolist.domain.members.repository.MemberRepository;
import com.app.todolist.domain.todos.repository.TodoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    public void clear() {
        todoRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("저장한 모든 Todo를 조회할 수 있다.")
    public void findByAllTest() {
        //given
        Member member = Member.create("tao@exemple.com", "1234");
        memberRepository.save(member);

        Todo todo1 = Todo.create(member, "todo-list", "hello");
        Todo todo2 = Todo.create(member, "todo-list", "hello");
        Todo todo3 = Todo.create(member, "todo-list", "hello");
        todoRepository.save(todo1);
        todoRepository.save(todo2);
        todoRepository.save(todo3);

        //when
        List<Todo> findTodo = todoRepository.findAll();

        //then
        Member findMember = findTodo.getLast().getMember();
        Assertions.assertThat(member.getEmail()).isEqualTo(findMember.getEmail());
        Assertions.assertThat(member.getPassword()).isEqualTo(findMember.getPassword());
        Assertions.assertThat(findTodo).hasSize(3);

    }

    @Test
    @DisplayName("회원이 저장한 Todo의 시간이 createdAt에 저장된다.")
    public void createdAtTest() {
        //given
        Member member = Member.create("tao@exemple.com", "1234");
        memberRepository.save(member);
        Todo todo = Todo.create(member, "todo-list", "hello");
        todoRepository.save(todo);

        //when
        Todo findTodo = todoRepository.findAll().stream().findFirst().orElseThrow();

        //then
        Assertions.assertThat(findTodo.getCreatedAt()).isNotNull();

    }

    @Test
    @DisplayName("회원이 저장한 Todo의 시간이 updatedAt에 저장된다.")
    public void updatedAtTest() {
        //given
        Member member = Member.create("tao@exemple.com", "1234");
        memberRepository.save(member);
        Todo todo = Todo.create(member, "todo-list", "hello");
        todoRepository.save(todo);

        //when
        Todo findTodo = todoRepository.findAll().stream().findFirst().orElseThrow();

        //then
        Assertions.assertThat(findTodo.getUpdatedAt()).isNotNull();

    }
}
