package com.example.camel;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class CamelSpringBootApplicationTests {

    @Test
    void contextLoads() {
        // Test que le contexte Spring se charge correctement
    }
}
