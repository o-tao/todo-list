package com.app.todolist.domain.todos.repository;

import com.app.todolist.api.todos.dto.TodoSearchRequest;
import com.app.todolist.domain.members.Member;
import com.app.todolist.domain.members.repository.MemberRepository;
import com.app.todolist.domain.todos.Todo;
import com.app.todolist.domain.todos.TodoStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class TodoCustomRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private TodoCustomRepository todoCustomRepository;

    @AfterEach
    void clear() {
        todoRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("todo를 검색할 수 있다.")
    public void todoSearchTest() {
        // given
        Member member = Member.create("tao@exemple.com", "1234");
        memberRepository.save(member);

        Todo todo = Todo.create(member, "todo title", "hello");
        todoRepository.save(todo);

        TodoSearchRequest searchRequest = new TodoSearchRequest();
        searchRequest.setMemberId(member.getId());
        searchRequest.setTitle("title");
        searchRequest.setStatus(TodoStatus.TODO);

        // when
        List<Todo> titleContains = todoCustomRepository.findByTitleContains(
                searchRequest.getMemberId(), searchRequest.getTitle(), searchRequest.getStatus());

        // then
        Assertions.assertThat(titleContains.stream().map(Todo -> todo.getMember().getId())).containsExactly(todo.getMember().getId());
        Assertions.assertThat(titleContains).extracting(searchRequest.getTitle()).containsExactly(todo.getTitle());
        Assertions.assertThat(titleContains.stream().map(Todo::getStatus)).containsExactly(todo.getStatus());
    }

    @Test
    @DisplayName("todo검색 시 title은 nullable할 수 있다.")
    public void todoSearchTitleNullableTrueTest() {
        // given
        Member member = Member.create("tao@exemple.com", "1234");
        memberRepository.save(member);

        Todo todo = Todo.create(member, "todo title", "hello");
        todoRepository.save(todo);

        TodoSearchRequest searchRequest = new TodoSearchRequest();
        searchRequest.setMemberId(member.getId());
        searchRequest.setTitle(null);
        searchRequest.setStatus(TodoStatus.TODO);

        // when
        List<Todo> titleContains = todoCustomRepository.findByTitleContains(
                searchRequest.getMemberId(), searchRequest.getTitle(), searchRequest.getStatus());

        // then
        Assertions.assertThat(titleContains.stream().map(Todo -> todo.getMember().getId())).containsExactly(todo.getMember().getId());
        Assertions.assertThat(titleContains.stream().map(Todo::getTitle)).containsExactly(todo.getTitle());
        Assertions.assertThat(titleContains.stream().map(Todo::getStatus)).containsExactly(todo.getStatus());
    }

    @Test
    @DisplayName("todo검색 시 title은 빈값으로 들어올 수 있다.")
    public void todoSearchTitleIsEmptyTrueTest() {
        // given
        Member member = Member.create("tao@exemple.com", "1234");
        memberRepository.save(member);

        Todo todo = Todo.create(member, "todo title", "hello");
        todoRepository.save(todo);

        TodoSearchRequest searchRequest = new TodoSearchRequest();
        searchRequest.setMemberId(member.getId());
        searchRequest.setTitle("");
        searchRequest.setStatus(TodoStatus.TODO);

        // when
        List<Todo> titleContains = todoCustomRepository.findByTitleContains(
                searchRequest.getMemberId(), searchRequest.getTitle(), searchRequest.getStatus());

        // then
        Assertions.assertThat(titleContains.stream().map(Todo -> todo.getMember().getId())).containsExactly(todo.getMember().getId());
        Assertions.assertThat(titleContains.stream().map(Todo::getTitle)).containsExactly(todo.getTitle());
        Assertions.assertThat(titleContains.stream().map(Todo::getStatus)).containsExactly(todo.getStatus());
    }

    @Test
    @DisplayName("todo검색 시 status는 nullable할 수 있다.")
    public void todoSearchStatusNullableTrueTest() {
        // given
        Member member = Member.create("tao@exemple.com", "1234");
        memberRepository.save(member);

        Todo todo = Todo.create(member, "todo title", "hello");
        todoRepository.save(todo);

        TodoSearchRequest searchRequest = new TodoSearchRequest();
        searchRequest.setMemberId(member.getId());
        searchRequest.setTitle("title");
        searchRequest.setStatus(null);

        // when
        List<Todo> titleContains = todoCustomRepository.findByTitleContains(
                searchRequest.getMemberId(), searchRequest.getTitle(), searchRequest.getStatus());

        // then
        Assertions.assertThat(titleContains.stream().map(Todo -> todo.getMember().getId())).containsExactly(todo.getMember().getId());
        Assertions.assertThat(titleContains.stream().map(Todo::getTitle)).containsExactly(todo.getTitle());
        Assertions.assertThat(titleContains.stream().map(Todo::getStatus)).containsExactly(todo.getStatus());
    }
}
