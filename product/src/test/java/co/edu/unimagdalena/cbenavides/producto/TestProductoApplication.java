package co.edu.unimagdalena.cbenavides.product;

import org.springframework.boot.SpringApplication;

public class TestProductoApplication {

    public static void main(String[] args) {
        SpringApplication.from(ProductoApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
