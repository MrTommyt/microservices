package co.edu.unimagdalena.cbenavides.product;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class ProductoApplicationTests {

    @Test
    void contextLoads() {
    }

}
