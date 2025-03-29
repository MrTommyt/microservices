package co.edu.unimagdalena.cbenavides.product.service;

import co.edu.unimagdalena.cbenavides.product.dto.ProductDto;
import co.edu.unimagdalena.cbenavides.product.entity.Product;
import co.edu.unimagdalena.cbenavides.product.repository.ProductRepository;
import co.edu.unimagdalena.cbenavides.product.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {

        UUID productId = UUID.randomUUID();
        String productName = "Coca-Cola";
        Double productPrice = 3500.0;
        String productCategory = "Refresco";
        String productDescription = "Bebida super refrescante";

        product = new Product(productId, productName, productPrice, productCategory, productDescription);
        productDto = new ProductDto(productId, productName, productPrice, productCategory, productDescription);
    }

    //test 1: findAll
    @Test
    void shouldReturnAllOrders() {
        //Given
        Mockito.when(productRepository.findAll()).thenReturn(List.of(product));

        //When
        Flux<ProductDto> result = productServiceImpl.findAll();

        //Then
        assertNotNull(result);
        assertEquals(1, result.count().block());
        assertEquals(product.getId(), result.elementAt(0, new ProductDto()).block().getId());
        verify(productRepository, times(1)).findAll();
    }
}