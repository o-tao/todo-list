package com.app.todolist.api.members;

import com.app.todolist.api.members.service.MemberService;
import com.app.todolist.api.members.service.dto.MemberLoginInfo;
import com.app.todolist.config.redis.dto.MemberSession;
import com.app.todolist.domain.members.Member;
import com.app.todolist.domain.members.repository.MemberRepository;
import com.app.todolist.web.exception.ErrorCode;
import com.app.todolist.web.exception.TodoApplicationException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RedisTemplate<String, MemberSession> redisTemplate;

    @AfterEach
    public void clear() {
        memberRepository.deleteAllInBatch();
    }

    private MemberLoginInfo loginMember(String email, String password) {
        return new MemberLoginInfo(email, password);
    }

    private Member createTestMember(String email, String password) {
        Member member = Member.create(email, password);
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("로그인 아이디와 비밀번호로 회원을 생성한다.")
    public void createMemberMemberTest() {
        // given
        Member member1 = Member.create("tao@exemple.com", "1234");
        memberService.createMember(member1);

        // when
        Member findMember = memberRepository.findAll().stream().findFirst().orElseThrow();

        // then
        assertThat(findMember.getEmail()).isEqualTo(member1.getEmail());
        assertThat(findMember.getPassword()).isEqualTo(member1.getPassword());
    }

    @Test
    @DisplayName("로그인 아이디가 존재할 경우 예외가 발생한다.")
    public void validateMemberTest() {
        // given
        Member member1 = Member.create("tao@exemple.com", "1234");
        Member member2 = Member.create("tao@exemple.com", "1234");
        memberService.createMember(member1);

        // when
        TodoApplicationException exception = assertThrows(TodoApplicationException.class, () -> memberService.createMember(member2));

        // then
        assertThat(exception.getClass()).isEqualTo(TodoApplicationException.class);
        assertThat(exception.getExceptionMessage()).isEqualTo("이미 가입된 이메일이 존재합니다.");
    }

    @Test
    @DisplayName("회원이 로그인 시 Redis에 세션이 생성된다.")
    public void sessionCreationTest() {
        // given
        Member member = createTestMember("tao@exemple.com", "1234");
        MemberLoginInfo memberLoginInfo = loginMember("tao@exemple.com", "1234");

        // when
        Cookie cookie = memberService.login(memberLoginInfo);
        String sessionId = cookie.getValue();
        String sessionKey = "TODO_SESSION:" + sessionId;

        // then
        MemberSession memberSession = redisTemplate.opsForValue().get(sessionKey);

        assertNotNull(memberSession);
        assertEquals(member.getId(), memberSession.getId());
    }

    @Test
    @DisplayName("회원이 로그인 시 쿠키가 생성된다.")
    public void cookieCreationTest() {
        // given
        createTestMember("tao@exemple.com", "1234");
        MemberLoginInfo memberLoginInfo = loginMember("tao@exemple.com", "1234");

        // when
        Cookie cookie = memberService.login(memberLoginInfo);

        // then
        assertNotNull(cookie);
        assertEquals("SESSION", cookie.getName());
        assertEquals(1800, cookie.getMaxAge());
        assertTrue(cookie.isHttpOnly());
        assertEquals("/", cookie.getPath());
    }

    @Test
    @DisplayName("로그인 시 이메일 또는 비밀번호가 틀리면 예외가 발생한다.")
    public void loginValidTest() {
        // given
        createTestMember("tao@exemple.com", "1234");
        MemberLoginInfo memberLoginInfo = loginMember("valid@exemple.com", "1");

        // when
        TodoApplicationException exception = assertThrows(TodoApplicationException.class, ()
                -> memberService.login(memberLoginInfo));

        // then
        assertThat(exception.getClass()).isEqualTo(TodoApplicationException.class);
        assertThat(exception.getExceptionMessage()).isEqualTo(ErrorCode.AUTHENTICATION_FAILED.getMessage());
    }
}
