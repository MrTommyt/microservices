package co.edu.unimagdalena.cbenavides.product.service;


import co.edu.unimagdalena.cbenavides.product.dto.ProductDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<ProductDto> findAll();
    Mono<ProductDto> findById(UUID id);
    ProductDto create(ProductDto productDto);
    Flux<ProductDto> update(int id, ProductDto newProductDto);
    void delete(int id);
    List<ProductDto> findByName(String name);
}
