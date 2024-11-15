package com.app.todolist.api.members;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.assertj.core.api.Assertions.assertThat;

public class BcryptTest {

    @Test
    @DisplayName("회원 생성 시 비밀번호가 암호화되어 생성된다.")
    void testPasswordEncryption() {
        // given
        String password = "1234";

        // when
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // then
        assertThat(hashedPassword).isNotNull();
        assertThat(password).isNotEqualTo(hashedPassword);
        assertThat(BCrypt.checkpw(password, hashedPassword)).isTrue();
    }
}
