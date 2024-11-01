package com.app.todolist.api.todos.service;

import com.app.todolist.api.todos.controller.dto.TodoSearchResponse;
import com.app.todolist.api.todos.service.dto.TodoUpdateInfo;
import com.app.todolist.api.todos.service.dto.TodosWithOptions;
import com.app.todolist.domain.members.Member;
import com.app.todolist.domain.members.repository.MemberRepository;
import com.app.todolist.domain.todos.Todo;
import com.app.todolist.domain.todos.TodoStatus;
import com.app.todolist.domain.todos.repository.TodoRepository;
import com.app.todolist.web.exception.TodoApplicationException;
import com.app.todolist.web.util.PaginationResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    private Member createMember() {
        Member member = Member.create("tao@exemple.com", "1234");
        return memberRepository.save(member);
    }

    private Todo createTodo(Member member, String title, String content) {
        Todo todo = Todo.create(member, title, content);
        return todoRepository.save(todo);
    }

    @Test
    @Transactional
    @DisplayName("존재하는 회원이 Todo생성 시 Todo가 저장된다.")
    public void createTodoTest() {
        // given
        Member savedMember = createMember();
        String title = "tao title";
        String content = "tao content";
        Long memberId = savedMember.getId();

        // when
        Todo createTodo = todoService.createTodo(memberId, title, content);

        // then
        Todo findTodo = todoRepository.findAll().stream().findFirst().orElseThrow();
        Member findMember = findTodo.getMember();
        assertThat(savedMember.getEmail()).isEqualTo(findMember.getEmail());
        assertThat(savedMember.getPassword()).isEqualTo(findMember.getPassword());
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
    @DisplayName("회원 본인이 생성한 Todo를 검색하면 검색한 회원의 Todo가 조회된다.")
    public void todoSearchTest() {
        // given
        Member savedMember = createMember();

        Todo todo = createTodo(savedMember, "todo title", "hello");

        TodosWithOptions todosWithOptions = new TodosWithOptions(
                savedMember.getId(), "todo title", TodoStatus.TODO, 1, 10
        );

        // when
        PaginationResponse<TodoSearchResponse> searchedTodos = todoService.searchTodosByOptions(todosWithOptions);

        // then
        assertThat(searchedTodos.getContents()).hasSize(1);
        assertThat(searchedTodos.getContents())
                .extracting(TodoSearchResponse::getMemberId)
                .containsExactly(todo.getMember().getId());
        assertThat(searchedTodos.getContents()).extracting(TodoSearchResponse::getTitle).containsExactly(todo.getTitle());
        assertThat(searchedTodos.getContents()).extracting(TodoSearchResponse::getStatus).containsExactly(todo.getStatus());
    }

    @Test
    @DisplayName("title에 'tao'를 포함한 Todo 검색결과를 가져온다.")
    public void searchTodosByTitleContainsTest() {
        // given
        Member savedMember = createMember();

        createTodo(savedMember, "test todo title", "hello");
        createTodo(savedMember, "hello tao! title", "world");
        createTodo(savedMember, "hello world tao! title", "hello");
        createTodo(savedMember, "hello tao! test title", "test");
        createTodo(savedMember, "hello world title", "test");

        TodosWithOptions todosWithOptions = new TodosWithOptions(
                savedMember.getId(), "tao", TodoStatus.TODO, 1, 10
        );

        // when
        PaginationResponse<TodoSearchResponse> searchedTodos = todoService.searchTodosByOptions(todosWithOptions);

        // then
        assertThat(searchedTodos.getContents()).hasSize(3);
        assertThat(searchedTodos.getContents()).extracting(TodoSearchResponse::getTitle).allMatch(title -> title.contains("tao"));
    }

    @Test
    @DisplayName("title에 'tao'를 포함한 Todo가 없을 경우 빈 리스트를 반환한다.")
    public void searchTodosByTitleContains_isEmpty() {
        // given
        Member savedMember = createMember();

        createTodo(savedMember, "test todo title", "hello");
        createTodo(savedMember, "hello! title", "world");
        createTodo(savedMember, "hello world! title", "hello");
        createTodo(savedMember, "hello! test title", "test");
        createTodo(savedMember, "hello world title", "test");

        TodosWithOptions todosWithOptions = new TodosWithOptions(
                savedMember.getId(), "tao", TodoStatus.TODO, 1, 10
        );

        // when
        PaginationResponse<TodoSearchResponse> searchedTodos = todoService.searchTodosByOptions(todosWithOptions);

        // then
        assertThat(searchedTodos.getContents()).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 회원이 todo를 검색시 예외가 발생한다.")
    public void todoSearchMemberValidateTest() {
        // given
        TodosWithOptions todosWithOptions = new TodosWithOptions(
                -1L, "todo title", TodoStatus.TODO, 1, 10
        );

        // when
        TodoApplicationException exception = assertThrows(TodoApplicationException.class,
                () -> todoService.searchTodosByOptions(todosWithOptions));

        // then
        assertThat(exception).isInstanceOf(TodoApplicationException.class);
        assertThat(exception.getExceptionMessage()).isEqualTo("회원이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("존재하는 todo의 title만 변경 될 경우 title만 수정된다.")
    public void todoTitleUpdateTest() {
        // given
        Member savedMember = createMember();
        Todo existingTodo = createTodo(savedMember, "todo-list", "hello");

        Long todoId = existingTodo.getId();
        TodoUpdateInfo updateInfo = new TodoUpdateInfo("title update", "hello");

        // when
        todoService.updateTodo(todoId, updateInfo);

        // then
        Todo updatedTodo = todoRepository.findById(todoId).orElseThrow();
        assertEquals(updateInfo.getTitle(), updatedTodo.getTitle());
        assertEquals(existingTodo.getContent(), updatedTodo.getContent());
    }

    @Test
    @DisplayName("존재하는 todo의 content만 변경 될 경우 content만 수정된다.")
    public void todoContentUpdateTest() {
        // given
        Member savedMember = createMember();
        Todo existingTodo = createTodo(savedMember, "todo-list", "hello");

        Long todoId = existingTodo.getId();
        TodoUpdateInfo updateInfo = new TodoUpdateInfo("todo-list", "content update");

        // when
        todoService.updateTodo(todoId, updateInfo);

        // then
        Todo updatedTodo = todoRepository.findById(todoId).orElseThrow();
        assertEquals(existingTodo.getTitle(), updatedTodo.getTitle());
        assertEquals(updateInfo.getContent(), updatedTodo.getContent());
    }

    @Test
    @DisplayName("존재하는 todo의 title과 content가 모두 변경될 경우 모두 수정된다.")
    public void todoTitleAndContentUpdateTest() {
        // given
        Member savedMember = createMember();
        Todo existingTodo = createTodo(savedMember, "todo-list", "hello");

        Long todoId = existingTodo.getId();
        TodoUpdateInfo updateInfo = new TodoUpdateInfo("title update", "content update");

        // when
        todoService.updateTodo(todoId, updateInfo);

        // then
        Todo updatedTodo = todoRepository.findById(todoId).orElseThrow();
        assertEquals(updateInfo.getTitle(), updatedTodo.getTitle());
        assertEquals(updateInfo.getContent(), updatedTodo.getContent());
    }

    @Test
    @DisplayName("존재하는 todo가 수정 될 경우 todo를 수정 한 시간이 updatedAt에 저장된다.")
    public void todoUpdatedAtTest() {
        // given
        Member savedMember = createMember();
        Todo existingTodo = createTodo(savedMember, "todo-list", "hello");

        Long todoId = existingTodo.getId();
        LocalDateTime previousUpdatedAt = existingTodo.getUpdatedAt();
        TodoUpdateInfo updateInfo = new TodoUpdateInfo("title update", "content update");

        // when
        todoService.updateTodo(todoId, updateInfo);

        // then
        Todo updatedTodo = todoRepository.findById(todoId).orElseThrow();
        assertThat(updatedTodo.getUpdatedAt()).isNotNull();
        assertThat(updatedTodo.getUpdatedAt()).isAfter(previousUpdatedAt);
    }

    @Test
    @DisplayName("존재하지 않는 todo 수정 시 예외가 발생한다.")
    public void todoUpdatedValidateTest() {
        // given
        TodoUpdateInfo updateInfo = new TodoUpdateInfo("title update", "content update");

        // when
        TodoApplicationException exception = assertThrows(TodoApplicationException.class,
                () -> todoService.updateTodo(-1L, updateInfo));

        // then
        assertThat(exception).isInstanceOf(TodoApplicationException.class);
        assertThat(exception.getExceptionMessage()).isEqualTo("TODO가 존재하지 않습니다.");
    }

}
