package com.app.todolist.api.members;

import com.app.todolist.domain.members.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("로그인 아이디가 존재할 경우 예외가 발생한다.")
    public void validateMemberTest() {
        // given
        Member member1 = Member.create("tao@exemple.com", "1234");
        Member member2 = Member.create("tao@exemple.com", "1234");

        // when
        memberService.create(member1);

        // then
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.create(member2));

        assertThat(e.getClass()).isEqualTo(IllegalStateException.class);
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

    }

}