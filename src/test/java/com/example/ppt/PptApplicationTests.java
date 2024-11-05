package com.example.ppt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PptApplicationTests {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertThat(applicationContext).isNotNull();
    }
    @Test
    void testMainBeanLoads() {
        // Assert that the main application class is loaded as a bean
        assertThat(applicationContext.containsBean("pptApplication")).isTrue();
    }
}
