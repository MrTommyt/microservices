package co.edu.unimagdalena.cbenavides.order.service;

import co.edu.unimagdalena.cbenavides.order.dto.OrderDto;
import co.edu.unimagdalena.cbenavides.order.entity.Order;
import co.edu.unimagdalena.cbenavides.order.mapper.OrderMapper;
import co.edu.unimagdalena.cbenavides.order.repository.OrderRepository;
import co.edu.unimagdalena.cbenavides.order.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;
    private OrderDto orderDto;

    @BeforeEach
    void setUp() {

        UUID orderId = UUID.randomUUID();
        LocalDateTime orderDate = LocalDateTime.now();
        String orderStatus = "on the way";
        Double amount = 120.0;

        order = new Order(orderId, orderDate, orderStatus, amount);
        orderDto = new OrderDto(orderId, orderDate, orderStatus, amount);
    }

    //Test 1: findAll
    @Test
    void shouldReturnAllOrders () {
        //Given
        Mockito.when(orderRepository.findAll()).thenReturn(List.of(order));

        //When
        Flux<OrderDto> result = orderService.findAll();

        //Then
        assertNotNull(result);
        assertEquals(1, result.count().block());
        assertEquals(order.getId(), result.elementAt(0, new OrderDto()).block().getId());
        verify(orderRepository, times(1)).findAll();
    }

    // Test 2: findById
    @Test
    void givenOrderId_whenFindById_thenReturnOrder() {
        // Given
        UUID id = order.getId();
        when(orderRepository.findById(id)).thenReturn(Optional.of(order));

        // When
        OrderDto result = orderService.findById(id).block();

        // Then
        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        assertEquals(order.getOrderDate(), result.getOrderDate());
        assertEquals(order.getStatus(), result.getStatus());
        assertEquals(order.getTotalAmount(), result.getTotalAmount());
        verify(orderRepository, times(1)).findById(id);
    }

    // Test 3: findById with non-existing ID
    @Test
    void givenNonExistingOrderId_thenReturnEmpty() {
        // Given
        UUID id = UUID.randomUUID();
        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Mono<OrderDto> result = orderService.findById(id);

        // Then
        assertNotNull(result);
        assertNull(result.block());
        verify(orderRepository, times(1)).findById(id);
    }

    // Test 4: save order
    @Test
    void givenOrderDto_whenSave_thenPersistAndReturnOrder() {
        // Given
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // When
        OrderDto result = orderService.save(orderDto).block();

        // Then
        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        assertEquals(order.getOrderDate(), result.getOrderDate());
        assertEquals(order.getStatus(), result.getStatus());
        assertEquals(order.getTotalAmount(), result.getTotalAmount());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    // Test 5: update existing order
    @Test
    void givenValidOrderIdAndOrderDto_whenUpdate_thenReturnUpdatedOrder() {
        // Given
        UUID id = UUID.randomUUID();

        OrderDto newOrderDto = new OrderDto(id, LocalDateTime.now(), "COMPLETED", 500.0);

        when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // When
        OrderDto result = orderService.update(id, newOrderDto).block();

        // Then
        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        verify(orderRepository, times(1)).findById(id);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    // Test 6: update non-existing order
    @Test
    void givenNonExistingOrderIdAndOrderDto_whenUpdate_thenReturnEmpty() {
        // Given
        UUID id = UUID.randomUUID();
        OrderDto newOrderDto = new OrderDto(id, LocalDateTime.now(), "COMPLETED", 500.0);
        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        // When
        OrderDto result = orderService.update(id, newOrderDto).block();

        // Then
        verify(orderRepository, times(1)).findById(id);
        verify(orderRepository, times(0)).save(any(Order.class));
    }

    // Test 7: delete order
    @Test
    void givenValidOrderId_whenDelete_thenCallDeleteOnRepository() {
        // Given
        UUID id = UUID.randomUUID();
        doNothing().when(orderRepository).deleteById(id);

        // When
        orderService.delete(id);

        // Then
        verify(orderRepository, times(1)).deleteById(id);
    }

    // Test 8: delete non-existing order
    @Test
    void givenNonExistingOrderId_whenDelete_thenDoNothing() {
        // Given
        UUID id = UUID.randomUUID();
        doNothing().when(orderRepository).deleteById(id);

        // When
        orderService.delete(id);

        // Then
        verify(orderRepository, times(1)).deleteById(id);
    }
}