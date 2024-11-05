package com.example.ppt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
class PptApplicationTests {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertThat(applicationContext).isNotNull();
    }
    @Test
    void testMainMethod() {
        // Use assertThatCode to ensure the main method runs without throwing exceptions
        assertThatCode(() -> PptApplication.main(new String[] {})).doesNotThrowAnyException();
    }
}
