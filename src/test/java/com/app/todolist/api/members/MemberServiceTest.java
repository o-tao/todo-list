package com.app.todolist.api.members;

import com.app.todolist.domain.members.Member;
import com.app.todolist.domain.members.MemberRepository;
import com.app.todolist.exception.TodoApplicationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    public void clear() {
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("로그인 아이디와 비밀번호로 회원을 생성한다.")
    public void CreateMemberTest() {
        // given
        Member member1 = Member.create("tao@exemple.com", "1234");
        memberService.create(member1);

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
        memberService.create(member1);

        // when
        TodoApplicationException exception = assertThrows(TodoApplicationException.class, () -> memberService.create(member2));

        // then
        assertThat(exception.getClass()).isEqualTo(TodoApplicationException.class);
        assertThat(exception.getExceptionMessage()).isEqualTo("이미 가입된 이메일이 존재합니다.");
    }

}