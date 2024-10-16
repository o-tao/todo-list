package com.app.todolist.domain.todos.repository;

import com.app.todolist.api.todos.controller.dto.TodoSearchRequest;
import com.app.todolist.domain.members.Member;
import com.app.todolist.domain.members.repository.MemberRepository;
import com.app.todolist.domain.todos.Todo;
import com.app.todolist.domain.todos.TodoStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TodoQueryRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private TodoQueryRepository todoQueryRepository;

    @AfterEach
    void clear() {
        todoRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("회원이 생성된 Todo를 검색하면 조회된다.")
    public void todoSearchTest() {
        // given
        Member member = Member.create("tao@exemple.com", "1234");
        memberRepository.save(member);

        Todo todo = Todo.create(member, "todo title", "hello");
        todoRepository.save(todo);

        TodoSearchRequest searchRequest = new TodoSearchRequest();
        searchRequest.setMemberId(member.getId());
        searchRequest.setTitle("todo title");
        searchRequest.setStatus(TodoStatus.TODO);

        // when
        List<Todo> titleContains = todoQueryRepository.findTodosByOptions(searchRequest.toOption());

        // then
        assertThat(titleContains).hasSize(1);
        assertThat(titleContains)
                .extracting(Todo::getMember)
                .extracting(Member::getId)
                .containsExactly(todo.getMember().getId());
        assertThat(titleContains).extracting(Todo::getTitle).containsExactly(todo.getTitle());
        assertThat(titleContains).extracting(Todo::getStatus).containsExactly(todo.getStatus());
    }

    @Test
    @DisplayName("title에 'tao'를 포함한 Todo 검색결과를 가져온다.")
    public void searchTodosByTitleContainsTest() {
        // given
        Member member = Member.create("tao@exemple.com", "1234");
        memberRepository.save(member);

        Todo todo1 = Todo.create(member, "test todo title", "hello");
        Todo todo2 = Todo.create(member, "hello tao! title", "world");
        Todo todo3 = Todo.create(member, "hello world tao! title", "hello");
        Todo todo4 = Todo.create(member, "hello tao! test title", "test");
        Todo todo5 = Todo.create(member, "hello world title", "test");
        todoRepository.saveAll(List.of(todo1, todo2, todo3, todo4, todo5));

        TodoSearchRequest searchRequest = new TodoSearchRequest();
        searchRequest.setMemberId(member.getId());
        searchRequest.setTitle("tao");
        searchRequest.setStatus(TodoStatus.TODO);

        // when
        List<Todo> titleContains = todoQueryRepository.findTodosByOptions(searchRequest.toOption());

        // then
        assertThat(titleContains).hasSize(3);
        assertThat(titleContains).extracting(Todo::getTitle).allMatch(title -> title.contains("tao"));
    }

    @Test
    @DisplayName("title에 'tao'를 포함한 Todo가 없을 경우 빈 리스트를 반환한다.")
    public void searchTodosByTitleContains_isEmpty() {
        // given
        Member member = Member.create("tao@exemple.com", "1234");
        memberRepository.save(member);

        Todo todo1 = Todo.create(member, "test todo title", "hello");
        Todo todo2 = Todo.create(member, "hello! title", "world");
        Todo todo3 = Todo.create(member, "hello world! title", "hello");
        Todo todo4 = Todo.create(member, "hello! test title", "test");
        Todo todo5 = Todo.create(member, "hello world title", "test");
        todoRepository.saveAll(List.of(todo1, todo2, todo3, todo4, todo5));

        TodoSearchRequest searchRequest = new TodoSearchRequest();
        searchRequest.setMemberId(member.getId());
        searchRequest.setTitle("tao");
        searchRequest.setStatus(TodoStatus.TODO);

        // when
        List<Todo> titleContains = todoQueryRepository.findTodosByOptions(searchRequest.toOption());

        // then
        assertThat(titleContains).isEmpty();
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
        List<Todo> titleContains = todoQueryRepository.findTodosByOptions(searchRequest.toOption());

        // then
        assertThat(titleContains).hasSize(1);
        assertThat(titleContains)
                .extracting(Todo::getMember)
                .extracting(Member::getId)
                .containsExactly(todo.getMember().getId());
        assertThat(titleContains).extracting(Todo::getTitle).containsExactly(todo.getTitle());
        assertThat(titleContains).extracting(Todo::getStatus).containsExactly(todo.getStatus());
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
        List<Todo> titleContains = todoQueryRepository.findTodosByOptions(searchRequest.toOption());

        // then
        assertThat(titleContains).hasSize(1);
        assertThat(titleContains)
                .extracting(Todo::getMember)
                .extracting(Member::getId)
                .containsExactly(todo.getMember().getId());
        assertThat(titleContains).extracting(Todo::getTitle).containsExactly(todo.getTitle());
        assertThat(titleContains).extracting(Todo::getStatus).containsExactly(todo.getStatus());
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
        searchRequest.setTitle("todo title");
        searchRequest.setStatus(null);

        // when
        List<Todo> titleContains = todoQueryRepository.findTodosByOptions(searchRequest.toOption());

        // then
        assertThat(titleContains).hasSize(1);
        assertThat(titleContains)
                .extracting(Todo::getMember)
                .extracting(Member::getId)
                .containsExactly(todo.getMember().getId());
        assertThat(titleContains).extracting(Todo::getTitle).containsExactly(todo.getTitle());
        assertThat(titleContains).extracting(Todo::getStatus).containsExactly(todo.getStatus());
    }

    @Test
    @DisplayName("'tao'가 포함된 title 검색 시 정상적으로 총 페이지 수가 생성된다.")
    public void todoSearchTotalPagesTest() {
        // given
        Member member = Member.create("tao@exemple.com", "1234");
        memberRepository.save(member);

        Todo todo1 = Todo.create(member, "todo tao title", "hello");
        Todo todo2 = Todo.create(member, "tao title", "hello");
        Todo todo3 = Todo.create(member, "title hello tao", "hello");
        todoRepository.saveAll(List.of(todo1, todo2, todo3));

        TodoSearchRequest searchRequest = new TodoSearchRequest();
        searchRequest.setMemberId(member.getId());
        searchRequest.setTitle("tao");
        searchRequest.setStatus(TodoStatus.TODO);
        searchRequest.setSize(1);
        long totalElements = todoQueryRepository.countByTodo(searchRequest.toOption());
        int totalPages = (int) Math.ceil((double) totalElements / searchRequest.getSize());

        // when
        List<Todo> titleContains = todoQueryRepository.findTodosByOptions(searchRequest.toOption());

        // then
        assertThat(titleContains).hasSize(1);
        assertThat(totalElements).isEqualTo(3);
        assertThat(totalPages).isEqualTo(3);
    }

    @Test
    @DisplayName("'tao' 검색 시 정상적으로 페이지 별로 'tao'를 포함한 검색결과를 가져온다.")
    public void todoSearchResultsContainsTest() {
        // given
        Member member = Member.create("tao@exemple.com", "1234");
        memberRepository.save(member);

        Todo todo1 = Todo.create(member, "tao todo title", "hello");
        Todo todo2 = Todo.create(member, "tao title", "hello");
        Todo todo3 = Todo.create(member, "hello tao title", "hello");
        todoRepository.saveAll(List.of(todo1, todo2, todo3));

        TodoSearchRequest searchRequest = new TodoSearchRequest();
        searchRequest.setMemberId(member.getId());
        searchRequest.setTitle("tao");
        searchRequest.setStatus(TodoStatus.TODO);
        searchRequest.setSize(1);
        long totalElements = todoQueryRepository.countByTodo(searchRequest.toOption());
        int totalPages = (int) Math.ceil((double) totalElements / searchRequest.getSize());
        System.out.println(totalPages + "~~~~~~~~~~~~~~~~~~~~~!!!!!!!!!!!!!");

        // when
        List<Todo> titleContains = todoQueryRepository.findTodosByOptions(searchRequest.toOption());

        // then
        assertThat(titleContains).hasSize(1);
        for (int i = 1; i < totalPages; i++) {
            searchRequest.setPage(i);
            searchRequest.setSize(1);
            List<Todo> pageContent = todoQueryRepository.findTodosByOptions(searchRequest.toOption());

            assertThat(pageContent).isNotEmpty();
            assertThat(pageContent.getFirst().getTitle()).contains("tao");
        }
    }
}
