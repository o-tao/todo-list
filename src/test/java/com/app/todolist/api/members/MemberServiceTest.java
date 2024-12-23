package com.app.todolist.api.members;

import com.app.todolist.api.members.service.MemberService;
import com.app.todolist.api.members.service.dto.MemberCreateInfo;
import com.app.todolist.api.members.service.dto.MemberLoginInfo;
import com.app.todolist.api.session.service.SessionService;
import com.app.todolist.config.redis.dto.MemberSession;
import com.app.todolist.domain.members.Member;
import com.app.todolist.domain.members.repository.MemberRepository;
import com.app.todolist.web.exception.ErrorCode;
import com.app.todolist.web.exception.TodoApplicationException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    private final static String SESSION_PREFIX = "TODO_SESSION";
    private final static String COOKIE_NAME = "SESSION";

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RedisTemplate<String, MemberSession> redisTemplate;
    @Autowired
    private SessionService sessionService;

    @AfterEach
    public void clear() {
        memberRepository.deleteAllInBatch();
    }

    private MemberCreateInfo createMemberInfo(String email, String password) {
        return new MemberCreateInfo(email, password);
    }

    private MemberLoginInfo loginMember(String email, String password) {
        return new MemberLoginInfo(email, password);
    }

    private Member createTestMember(String email, String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        Member member = Member.create(email, hashedPassword);
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("로그인 아이디와 비밀번호로 회원을 생성한다.")
    public void createMemberTest() {
        // given
        MemberCreateInfo member = createMemberInfo("tao@exemple.com", "1234");
        memberService.createMember(member);

        // when
        Member findMember = memberRepository.findAll().stream().findFirst().orElseThrow();

        // then
        assertThat(findMember.getEmail()).isEqualTo(member.getEmail());
        assertThat(BCrypt.checkpw(member.getPassword(), findMember.getPassword())).isTrue();
    }

    @Test
    @DisplayName("회원 생성 시 비밀번호가 암호화되어 저장된다.")
    public void createMemberPasswordShouldBeEncryptedTest() {
        // given
        MemberCreateInfo memberCreateInfo = createMemberInfo("tao@exemple.com", "1234");

        // when
        Member createdMember = memberService.createMember(memberCreateInfo);

        // then
        assertThat(createdMember).isNotNull();
        assertThat(memberCreateInfo.getPassword()).isNotEqualTo(createdMember.getPassword());
        assertThat(BCrypt.checkpw(memberCreateInfo.getPassword(), createdMember.getPassword())).isTrue();
    }

    @Test
    @DisplayName("로그인 아이디가 존재할 경우 예외가 발생한다.")
    public void validateMemberTest() {
        // given
        MemberCreateInfo member1 = createMemberInfo("tao@exemple.com", "1234");
        MemberCreateInfo member2 = createMemberInfo("tao@exemple.com", "1234");
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

        // then
        MemberSession memberSession = sessionService.findSession(sessionId);
        assertThat(memberSession).isNotNull();
        assertThat(member.getId()).isEqualTo(memberSession.getMemberId());
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
        assertEquals(COOKIE_NAME, cookie.getName());
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

    @Test
    @DisplayName("로그인 된 사용자가 로그아웃 시 정상적으로 쿠키가 만료 처리된다.")
    public void logoutByCookieExpirationTest() {
        // given
        String sessionId = "testSessionId";
        redisTemplate.opsForValue().set(SESSION_PREFIX + sessionId, new MemberSession(1L));

        // when
        Cookie cookie = memberService.logout(sessionId);

        // then
        assertThat(cookie).isNotNull();
        assertThat(COOKIE_NAME).isEqualTo(cookie.getName());
        assertThat(0).isEqualTo(cookie.getMaxAge());
        assertThat(cookie.getValue()).isNull();
    }

    @Test
    @DisplayName("로그인 된 사용자가 로그아웃 시 정상적으로 Redis 세션이 제거된다.")
    public void logoutBySessionDeletionTest() {
        // given
        String sessionId = "testSessionId";
        redisTemplate.opsForValue().set(SESSION_PREFIX + sessionId, new MemberSession(1L));

        // when
        memberService.logout(sessionId);

        // then
        MemberSession memberSession = sessionService.findSession(sessionId);
        assertThat(memberSession).isNull();
    }
}
