package co.edu.unimagdalena.cbenavides.product.service;


import co.edu.unimagdalena.cbenavides.product.dto.ProductDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public interface ProductService {
    Flux<ProductDto> findAll();
    Mono<ProductDto> findById(UUID id);
    Mono<ProductDto> save(ProductDto productDto);
    Mono<ProductDto> update(UUID id, ProductDto newProductDto);
    void delete(UUID id);
    Mono<ProductDto> findByName(String name);
}
