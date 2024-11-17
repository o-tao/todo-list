package com.app.todolist.api.todos.service;

import com.app.todolist.api.todos.controller.dto.TodoSearchResponse;
import com.app.todolist.api.todos.service.dto.TodoCreateInfo;
import com.app.todolist.api.todos.service.dto.TodoUpdateInfo;
import com.app.todolist.api.todos.service.dto.TodosWithOptions;
import com.app.todolist.config.redis.dto.MemberSession;
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

    private MemberSession createMemberSession(Long memberId) {
        return new MemberSession(memberId);
    }

    @Test
    @Transactional
    @DisplayName("존재하는 회원이 Todo생성 시 Todo가 저장된다.")
    public void createTodoTest() {
        // given
        Member savedMember = createMember();
        TodoCreateInfo todoCreateInfo = new TodoCreateInfo("tao title", "tao content");
        Long memberId = savedMember.getId();

        // when
        Todo createTodo = todoService.createTodo(todoCreateInfo, memberId);

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
    public void createTodoByValidateMemberTest() {
        // given
        TodoCreateInfo todoCreateInfo = new TodoCreateInfo("tao title", "tao content");

        // when
        TodoApplicationException exception = assertThrows(TodoApplicationException.class,
                () -> todoService.createTodo(todoCreateInfo, -1L));

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
    @DisplayName("회원이 자신이 작성한 todo 수정 시 정상적으로 수정된다.")
    public void todoUpdateByValidMemberTest() {
        // given
        Member savedMember = createMember();
        Todo existingTodo = createTodo(savedMember, "todo-list", "hello");

        TodoUpdateInfo updateInfo = new TodoUpdateInfo("title update", "content update");

        MemberSession memberSession = createMemberSession(savedMember.getId());

        // when
        todoService.updateTodo(existingTodo.getId(), updateInfo, memberSession.getMemberId());

        // then
        Todo updatedTodo = todoRepository.findById(existingTodo.getId()).orElseThrow();
        assertEquals(updateInfo.getTitle(), updatedTodo.getTitle());
        assertEquals(updateInfo.getContent(), updatedTodo.getContent());
    }

    @Test
    @DisplayName("회원이 다른 사용자의 todo를 수정 시 예외가 발생한다.")
    public void todoUpdateByInvalidMemberTest() {
        // given
        Member savedMember = createMember();
        Todo existingTodo = createTodo(savedMember, "todo-list", "hello");

        TodoUpdateInfo updateInfo = new TodoUpdateInfo("title update", "content update");

        MemberSession memberSession = createMemberSession(-1L);

        // when
        TodoApplicationException exception = assertThrows(TodoApplicationException.class,
                () -> todoService.updateTodo(existingTodo.getId(), updateInfo, memberSession.getMemberId()));

        // then
        assertThat(exception).isInstanceOf(TodoApplicationException.class);
        assertThat(exception.getExceptionMessage()).isEqualTo("이 작업을 수행할 권한이 없습니다.");
    }

    @Test
    @DisplayName("Todo의 title을 수정한다.")
    public void todoTitleUpdateTest() {
        // given
        Member savedMember = createMember();
        Todo existingTodo = createTodo(savedMember, "todo-list", "hello");

        MemberSession memberSession = createMemberSession(savedMember.getId());

        Long todoId = existingTodo.getId();
        TodoUpdateInfo updateInfo = new TodoUpdateInfo("title update", "hello");


        // when
        todoService.updateTodo(todoId, updateInfo, memberSession.getMemberId());

        // then
        Todo updatedTodo = todoRepository.findById(todoId).orElseThrow();
        assertEquals(updateInfo.getTitle(), updatedTodo.getTitle());
        assertEquals(existingTodo.getContent(), updatedTodo.getContent());
    }

    @Test
    @DisplayName("Todo의 content를 수정한다.")
    public void todoContentUpdateTest() {
        // given
        Member savedMember = createMember();
        Todo existingTodo = createTodo(savedMember, "todo-list", "hello");

        MemberSession memberSession = createMemberSession(savedMember.getId());

        Long todoId = existingTodo.getId();
        TodoUpdateInfo updateInfo = new TodoUpdateInfo("todo-list", "content update");

        // when
        todoService.updateTodo(todoId, updateInfo, memberSession.getMemberId());

        // then
        Todo updatedTodo = todoRepository.findById(todoId).orElseThrow();
        assertEquals(existingTodo.getTitle(), updatedTodo.getTitle());
        assertEquals(updateInfo.getContent(), updatedTodo.getContent());
    }

    @Test
    @DisplayName("Todo의 수정 시간이 updatedAt에 저장된다.")
    public void todoUpdatedAtTest() {
        // given
        Member savedMember = createMember();
        Todo existingTodo = createTodo(savedMember, "todo-list", "hello");

        MemberSession memberSession = createMemberSession(savedMember.getId());

        Long todoId = existingTodo.getId();
        LocalDateTime previousUpdatedAt = existingTodo.getUpdatedAt();
        TodoUpdateInfo updateInfo = new TodoUpdateInfo("title update", "content update");

        // when
        todoService.updateTodo(todoId, updateInfo, memberSession.getMemberId());

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

        MemberSession memberSession = createMemberSession(1L);

        // when
        TodoApplicationException exception = assertThrows(TodoApplicationException.class,
                () -> todoService.updateTodo(-1L, updateInfo, memberSession.getMemberId()));

        // then
        assertThat(exception).isInstanceOf(TodoApplicationException.class);
        assertThat(exception.getExceptionMessage()).isEqualTo("TODO가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("회원이 생성한 본인의 todo에 대해 단일 건으로 상세조회를 할 수 있다.")
    public void findTodoByDetailsTest() {
        // given
        Member savedMember = createMember();
        Todo existingTodo = createTodo(savedMember, "todo-list", "hello");

        MemberSession memberSession = createMemberSession(savedMember.getId());

        // when
        Todo todoDetails = todoService.getTodoDetails(existingTodo.getId(), memberSession.getMemberId());

        // then
        assertThat(existingTodo.getTitle()).isEqualTo(todoDetails.getTitle());
        assertThat(existingTodo.getContent()).isEqualTo(todoDetails.getContent());
        assertThat(existingTodo.getStatus()).isEqualTo(todoDetails.getStatus());
        assertThat(existingTodo.getCreatedAt()).isEqualTo(todoDetails.getCreatedAt());
        assertThat(existingTodo.getUpdatedAt()).isEqualTo(todoDetails.getUpdatedAt());
    }

    @Test
    @DisplayName("회원이 다른 사용자의 todo 상세 조회 시 예외가 발생한다.")
    public void findTodoByDetailsByInvalidMemberTest() {
        // given
        Member savedMember = createMember();
        Todo existingTodo = createTodo(savedMember, "todo-list", "hello");

        // when
        TodoApplicationException exception = assertThrows(TodoApplicationException.class,
                () -> todoService.getTodoDetails(existingTodo.getId(), -1L));

        // then
        assertThat(exception).isInstanceOf(TodoApplicationException.class);
        assertThat(exception.getExceptionMessage()).isEqualTo("이 작업을 수행할 권한이 없습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 todo 상세 조회 시 예외가 발생한다.")
    public void findTodoByDetailsValidateTest() {
        // given
        MemberSession memberSession = createMemberSession(1L);

        // when
        TodoApplicationException exception = assertThrows(TodoApplicationException.class,
                () -> todoService.getTodoDetails(-1L, memberSession.getMemberId()));

        // then
        assertThat(exception).isInstanceOf(TodoApplicationException.class);
        assertThat(exception.getExceptionMessage()).isEqualTo("TODO가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("회원이 본인의 todo의 상태 변경 시 정상적으로 변경된다.")
    public void todoStatusUpdateTest() {
        // given
        Member savedMember = createMember();
        Todo existingTodo = createTodo(savedMember, "todo-list", "hello");

        MemberSession memberSession = createMemberSession(savedMember.getId());

        // when
        todoService.updateTodoStatus(existingTodo.getId(), TodoStatus.DONE, memberSession.getMemberId());

        // then
        Todo updatedTodo = todoRepository.findById(existingTodo.getId()).orElseThrow();
        assertThat(updatedTodo.getStatus()).isEqualTo(TodoStatus.DONE);
    }

    @Test
    @DisplayName("회원이 다른 사용자의 todo 상태 변경 시 예외가 발생한다.")
    public void todoStatusUpdateByInvalidMemberTest() {
        // given
        Member savedMember = createMember();
        Todo existingTodo = createTodo(savedMember, "todo-list", "hello");

        MemberSession memberSession = createMemberSession(-1L);

        // when
        TodoApplicationException exception = assertThrows(TodoApplicationException.class,
                () -> todoService.updateTodoStatus(existingTodo.getId(), TodoStatus.DONE, memberSession.getMemberId()));

        // then
        assertThat(exception).isInstanceOf(TodoApplicationException.class);
        assertThat(exception.getExceptionMessage()).isEqualTo("이 작업을 수행할 권한이 없습니다.");
    }

    @Test
    @DisplayName("Todo의 상태 변경 된 시간이 updatedAt에 저장된다.")
    public void todoStatusUpdatedAtTest() {
        // given
        Member savedMember = createMember();
        Todo existingTodo = createTodo(savedMember, "todo-list", "hello");

        LocalDateTime previousUpdatedAt = existingTodo.getUpdatedAt();

        MemberSession memberSession = createMemberSession(savedMember.getId());

        // when
        todoService.updateTodoStatus(existingTodo.getId(), TodoStatus.DONE, memberSession.getMemberId());

        // then
        Todo updatedTodo = todoRepository.findById(existingTodo.getId()).orElseThrow();
        assertThat(updatedTodo.getUpdatedAt()).isNotNull();
        assertThat(updatedTodo.getUpdatedAt()).isAfter(previousUpdatedAt);
    }

}
