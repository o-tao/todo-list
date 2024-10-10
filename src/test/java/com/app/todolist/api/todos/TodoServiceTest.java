package com.app.todolist.api.todos;

import com.app.todolist.api.todos.dto.TodosWithOptions;
import com.app.todolist.domain.members.Member;
import com.app.todolist.domain.members.repository.MemberRepository;
import com.app.todolist.domain.todos.Todo;
import com.app.todolist.domain.todos.TodoStatus;
import com.app.todolist.domain.todos.repository.TodoRepository;
import com.app.todolist.exception.TodoApplicationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        memberRepository.deleteAllInBatch();
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
                () -> todoService.createTodo(-1L, title, content));

        // then
        assertThat(exception).isInstanceOf(TodoApplicationException.class);
        assertThat(exception.getExceptionMessage()).isEqualTo("회원이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("회원이 Todo를 검색한다.")
    public void todoSearchTest() {
        // given
        Member member = Member.create("tao@exemple.com", "1234");
        Member savedMember = memberRepository.save(member);

        Todo todo = todoService.createTodo(savedMember.getId(), "todo title", "hello");

        // when
        List<Todo> searchedTodos = todoService.searchTodosWithOptions(
                new TodosWithOptions(savedMember.getId(), "title", TodoStatus.TODO));

        // then
        assertThat(searchedTodos.size()).isEqualTo(1);
        assertThat(searchedTodos).extracting(Todo::getMember)
                .extracting(Member::getId).containsExactly(todo.getMember().getId());
        assertThat(searchedTodos).extracting(Todo::getTitle).containsExactly(todo.getTitle());
        assertThat(searchedTodos).extracting(Todo::getStatus).containsExactly(todo.getStatus());
    }

    @Test
    @DisplayName("title에 'tao'를 포함한 Todo 검색결과를 가져온다.")
    public void searchTodosByTitleContainsTest() {
        // given
        Member member = Member.create("tao@exemple.com", "1234");
        Member savedMember = memberRepository.save(member);

        todoService.createTodo(savedMember.getId(), "test todo title", "hello");
        todoService.createTodo(savedMember.getId(), "hello tao! title", "world");
        todoService.createTodo(savedMember.getId(), "hello world tao! title", "hello");
        todoService.createTodo(savedMember.getId(), "hello tao! test title", "test");
        todoService.createTodo(savedMember.getId(), "hello world title", "test");

        // when
        List<Todo> searchedTodos = todoService.searchTodosWithOptions(
                new TodosWithOptions(savedMember.getId(), "tao", TodoStatus.TODO));

        // then
        assertThat(searchedTodos).hasSize(3);
        assertThat(searchedTodos).extracting(Todo::getTitle).allMatch(title -> title.contains("tao"));
    }

    @Test
    @DisplayName("title에 'tao'를 포함한 Todo가 없을 경우 빈 리스트를 반환한다.")
    public void searchTodosByTitleContains_isEmpty() {
        // given
        Member member = Member.create("tao@exemple.com", "1234");
        Member savedMember = memberRepository.save(member);

        todoService.createTodo(savedMember.getId(), "test todo title", "hello");
        todoService.createTodo(savedMember.getId(), "hello! title", "world");
        todoService.createTodo(savedMember.getId(), "hello world! title", "hello");
        todoService.createTodo(savedMember.getId(), "hello! test title", "test");
        todoService.createTodo(savedMember.getId(), "hello world title", "test");

        // when
        List<Todo> searchedTodos = todoService.searchTodosWithOptions(
                new TodosWithOptions(savedMember.getId(), "tao", TodoStatus.TODO));

        // then
        assertThat(searchedTodos).isEmpty();
    }

    @Test
    @DisplayName("todo검색 시 title은 nullable할 수 있다.")
    public void todoSearchTitleNullableTrueTest() {
        // given
        Member member = Member.create("tao@exemple.com", "1234");
        Member savedMember = memberRepository.save(member);

        Todo todo = todoService.createTodo(savedMember.getId(), "todo title", "hello");

        // when
        List<Todo> searchedTodos = todoService.searchTodosWithOptions(
                new TodosWithOptions(savedMember.getId(), null, TodoStatus.TODO));

        // then
        assertThat(searchedTodos.size()).isEqualTo(1);
        assertThat(searchedTodos).extracting(Todo::getMember)
                .extracting(Member::getId).containsExactly(todo.getMember().getId());
        assertThat(searchedTodos).extracting(Todo::getTitle).containsExactly(todo.getTitle());
        assertThat(searchedTodos).extracting(Todo::getStatus).containsExactly(todo.getStatus());
    }

    @Test
    @DisplayName("todo검색 시 title은 빈값으로 들어올 수 있다.")
    public void todoSearchTitleIsEmptyTrueTest() {
        // given
        Member member = Member.create("tao@exemple.com", "1234");
        Member savedMember = memberRepository.save(member);

        Todo todo = todoService.createTodo(savedMember.getId(), "todo title", "hello");

        // when
        List<Todo> searchedTodos = todoService.searchTodosWithOptions(
                new TodosWithOptions(savedMember.getId(), "", TodoStatus.TODO));

        // then
        assertThat(searchedTodos.size()).isEqualTo(1);
        assertThat(searchedTodos).extracting(Todo::getMember)
                .extracting(Member::getId).containsExactly(todo.getMember().getId());
        assertThat(searchedTodos).extracting(Todo::getTitle).containsExactly(todo.getTitle());
        assertThat(searchedTodos).extracting(Todo::getStatus).containsExactly(todo.getStatus());
    }

    @Test
    @DisplayName("todo검색 시 status는 nullable할 수 있다.")
    public void todoSearchStatusNullableTrueTest() {
        // given
        Member member = Member.create("tao@exemple.com", "1234");
        Member savedMember = memberRepository.save(member);

        Todo todo = todoService.createTodo(savedMember.getId(), "todo title", "hello");

        // when
        List<Todo> searchedTodos = todoService.searchTodosWithOptions(
                new TodosWithOptions(savedMember.getId(), "title", null));

        // then
        assertThat(searchedTodos.size()).isEqualTo(1);
        assertThat(searchedTodos).extracting(Todo::getMember)
                .extracting(Member::getId).containsExactly(todo.getMember().getId());
        assertThat(searchedTodos).extracting(Todo::getTitle).containsExactly(todo.getTitle());
        assertThat(searchedTodos).extracting(Todo::getStatus).containsExactly(todo.getStatus());
    }

    @Test
    @DisplayName("존재하지 않는 회원이 todo를 검색시 예외가 발생한다.")
    public void todoSearchMemberValidateTest() {
        // given
        TodosWithOptions invalidMemberTodosWithOptions = new TodosWithOptions(
                -1L, "title", TodoStatus.TODO);

        // when
        TodoApplicationException exception = assertThrows(TodoApplicationException.class,
                () -> todoService.searchTodosWithOptions(invalidMemberTodosWithOptions));

        // then
        assertThat(exception).isInstanceOf(TodoApplicationException.class);
        assertThat(exception.getExceptionMessage()).isEqualTo("회원이 존재하지 않습니다.");
    }

}
