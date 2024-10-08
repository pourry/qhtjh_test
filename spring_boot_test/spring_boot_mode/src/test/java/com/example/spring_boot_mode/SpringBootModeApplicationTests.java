package com.example.spring_boot_mode;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

@SpringBootTest
class SpringBootModeApplicationTests {

    @Test
    void contextLoads() {
        String s = null;
        System.out.println(Objects.requireNonNull(s,"s是null"));
    }

}
