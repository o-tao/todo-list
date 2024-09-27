package com.app.todolist.api.members;

import com.app.todolist.api.members.dto.MemberRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberControllerTest {

    @Autowired
    Validator validator;

    @Test
    @DisplayName("아이디를 입력하지 않을 경우 예외가 발생한다.")
    public void idValidTest() {
        // given
        MemberRequest memberRequest = new MemberRequest();
        memberRequest.setEmail(null);
        memberRequest.setPassword("1234");

        // when
        Set<ConstraintViolation<MemberRequest>> violations = validator.validate(memberRequest);

        // then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("이메일을 입력하세요.")
                .hasSize(1);
    }

    @Test
    @DisplayName("비밀번호를 입력하지 않을 경우 예외가 발생한다.")
    public void passwordValidTest() {
        // given
        MemberRequest memberRequest = new MemberRequest();
        memberRequest.setEmail("tao@exemple.com");
        memberRequest.setPassword(null);

        // when
        Set<ConstraintViolation<MemberRequest>> violations = validator.validate(memberRequest);

        // then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("비밀번호를 입력하세요.")
                .hasSize(1);
    }

}
