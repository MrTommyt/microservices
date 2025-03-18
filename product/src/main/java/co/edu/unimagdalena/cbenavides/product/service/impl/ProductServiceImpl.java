package co.edu.unimagdalena.cbenavides.product.service.impl;

import co.edu.unimagdalena.cbenavides.product.dto.ProductDto;
import co.edu.unimagdalena.cbenavides.product.mapper.ProductMapper;
import co.edu.unimagdalena.cbenavides.product.repository.ProductRepository;
import co.edu.unimagdalena.cbenavides.product.service.ProductService;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper = ProductMapper.INSTANCE;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Flux<ProductDto> findAll() {
        return Flux.fromIterable(productRepository.findAll()).map(productMapper::toProductDto);
    }

    @Override
    public Mono<ProductDto> findById(UUID id) {
        return Mono.fromSupplier(() -> productRepository.findById(id).orElse(null)).map(productMapper::toProductDto);
    }

    @Override
    public Mono<ProductDto> save(ProductDto productDto) {
        return Mono.fromCallable(() -> productRepository.save(productMapper.toProduct(productDto)))
                .subscribeOn(Schedulers.boundedElastic())
                .map(productMapper::toProductDto);
    }

    @Override
    public Mono<ProductDto> update(UUID id, ProductDto newProductDto) {
        return Mono.fromSupplier(() -> productRepository.findById(id).map(prod -> {
            prod = productMapper.toProduct(newProductDto);
            prod = productRepository.save(prod);

            return prod;
        }).map(productMapper::toProductDto).orElse(null));
    }

    @Override
    public void delete(UUID id) {
        productRepository.deleteById(id);
    }

    @Override
    public Mono<ProductDto> findByName(String name) {
        return Mono.fromSupplier(() -> productRepository.findByName(name).orElse(null)).map(productMapper::toProductDto);
    }
}
