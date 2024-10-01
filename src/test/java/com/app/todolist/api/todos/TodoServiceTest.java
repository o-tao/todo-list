package com.app.todolist.api.todos;

import com.app.todolist.domain.members.Member;
import com.app.todolist.domain.members.MemberRepository;
import com.app.todolist.domain.todos.Todo;
import com.app.todolist.domain.todos.TodoRepository;
import com.app.todolist.exception.TodoApplicationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class TodoServiceTest {

    @Autowired
    private TodoService todoService;
    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    public void clear() {
        todoRepository.deleteAllInBatch();
    }

    @Test
    @Transactional
    @DisplayName("회원이 Todo를 생성한다.")
    public void createTodoTest() {
        // given
        Member member = Member.create("tao@exemple.com", "1234");
        Member savedMember = memberRepository.save(member);
        String title = "tao title";
        String content = "tao content";
        Long memberId = savedMember.getId();

        // when
        Todo createTodo = todoService.createTodo(memberId, title, content);

        // then
        Todo findTodo = todoRepository.findAll().stream().findFirst().orElseThrow();
        Member findMember = findTodo.getMember();
        assertThat(member.getEmail()).isEqualTo(findMember.getEmail());
        assertThat(member.getPassword()).isEqualTo(findMember.getPassword());
        assertThat(findTodo.getTitle()).isEqualTo(createTodo.getTitle());
        assertThat(findTodo.getContent()).isEqualTo(createTodo.getContent());
    }

    @Test
    @DisplayName("존재하지 않는 회원이 Todo생성 시 예외가 발생한다.")
    public void validateMemberTest() {
        // given
        String title = "tao title";
        String content = "tao content";

        // when
        TodoApplicationException exception = assertThrows(TodoApplicationException.class,
                () -> todoService.createTodo(1L, title, content));

        // then
        assertThat(exception.getClass()).isEqualTo(TodoApplicationException.class);
        assertThat(exception.getExceptionMessage()).isEqualTo("회원이 존재하지 않습니다.");
    }

}
