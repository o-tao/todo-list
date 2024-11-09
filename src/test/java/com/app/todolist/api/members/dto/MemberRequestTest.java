package com.app.todolist.api.members.dto;

import com.app.todolist.api.members.controller.dto.request.MemberRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRequestTest {

    private Validator validator = null;
    private MemberRequest memberRequest;

    @BeforeEach
    public void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        memberRequest = new MemberRequest();
    }

    @Test
    @DisplayName("이메일을 입력하지 않을 경우 예외가 발생한다.")
    public void idValidTest() {
        // given
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
    @DisplayName("이메일을 입력할 경우 예외가 발생하지 않는다.")
    public void idSuccessTest() {
        // given
        memberRequest.setEmail("tao@exemple.com");
        memberRequest.setPassword("1234");

        // when
        Set<ConstraintViolation<MemberRequest>> violations = validator.validate(memberRequest);

        // then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains()
                .hasSize(0);
    }

    @Test
    @DisplayName("비밀번호를 입력하지 않을 경우 예외가 발생한다.")
    public void passwordValidTest() {
        // given
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

    @Test
    @DisplayName("비밀번호를 입력할 경우 예외가 발생하지 않는다.")
    public void passwordSuccessTest() {
        // given
        memberRequest.setEmail("tao@exemple.com");
        memberRequest.setPassword("1234");

        // when
        Set<ConstraintViolation<MemberRequest>> violations = validator.validate(memberRequest);

        // then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains()
                .hasSize(0);
    }

    @Test
    @DisplayName("이메일 형식이 아닐 경우 예외가 발생한다.")
    public void emailPatternTest() {
        // given
        memberRequest.setEmail("tao123");
        memberRequest.setPassword("1234");

        // when
        Set<ConstraintViolation<MemberRequest>> violations = validator.validate(memberRequest);

        // then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("이메일 형식이 올바르지 않습니다.")
                .hasSize(1);
    }

    @Test
    @DisplayName("이메일 형식일 경우 예외가 발생하지 않는다.")
    public void emailPatternSuccessTest() {
        // given
        memberRequest.setEmail("tao123@exemple.com");
        memberRequest.setPassword("1234");

        // when
        Set<ConstraintViolation<MemberRequest>> violations = validator.validate(memberRequest);

        // then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains()
                .hasSize(0);
    }

}
