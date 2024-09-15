package com.app.todolist.domain.members;

import com.app.todolist.config.AuditingConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;


@DataJpaTest
@Import(AuditingConfig.class)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    public void clear() {
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("회원가입 한 member를 조회할 수 있다.")
    public void saveTest() {
        // given
        Member member = Member.create("tao@exemple.com", "1234");
        memberRepository.save(member);

        // when
        Member findMember = memberRepository.findAll().stream().findFirst().orElseThrow();

        // then
        Assertions.assertThat(findMember.getEmail()).isEqualTo(member.getEmail());
        Assertions.assertThat(findMember.getPassword()).isEqualTo(member.getPassword());
    }

    @Test
    @DisplayName("회원가입 시 생성시간이 createdAt에 저장된다.")
    public void saveCreatedAtTest() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Member member = Member.create("tao@exemple.com", "1234");
        memberRepository.save(member);

        // when
        Member findMember = memberRepository.findAll().stream().findFirst().orElseThrow();

        // then
        Assertions.assertThat(findMember.getCreatedAt().isAfter(now)).isTrue();
    }
}