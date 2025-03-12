package co.edu.unimagdalena.cbenavides.product.controller;


import co.edu.unimagdalena.cbenavides.product.dto.ProductDto;
import co.edu.unimagdalena.cbenavides.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public Flux<ProductDto> getAllProducts() {
        return productService.findAll();
    }

    @GetMapping("/products/{idProduct}")
    public Mono<ProductDto> getProductById(@PathVariable("idProduct") UUID idProduct) {
        return productService.findById(idProduct);
    }

    @PostMapping("/")
    public Mono<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        return productService.save(productDto);
    }

    @PutMapping("{id}")
    public Mono<ProductDto> updateProduct(@PathVariable("id") UUID id, @RequestBody ProductDto productDto) {
        return productService.update(id, productDto);
    }

    @DeleteMapping("{id}")
    public void deleteProduct(@PathVariable("id") UUID id) {
        productService.delete(id);
    }

    @GetMapping("{name}")
    public Mono<ProductDto> findByName(@PathVariable("name") String name) {
        return productService.findByName(name);
    }
}
