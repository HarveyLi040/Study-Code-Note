package com.lsl.code;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class ApplicationTests {

    @Test
    void contextLoads() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode("123");
        // 打印密码"123"加密后得到的结果
        System.out.println(encode); //$2a$10$BBp139W6cWAhWuA63vINsutEqaoCBH1DbIW7MbFa9dbV7fK42vwES
        // 判断原密码和加密后的密码是否匹配
        boolean matches = passwordEncoder.matches("123",encode);
        System.out.println(matches); // true
    }

}
