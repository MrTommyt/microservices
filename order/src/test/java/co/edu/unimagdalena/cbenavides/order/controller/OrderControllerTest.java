package co.edu.unimagdalena.cbenavides.order.controller;

import co.edu.unimagdalena.cbenavides.order.dto.OrderDto;
import co.edu.unimagdalena.cbenavides.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private UUID orderId;
    private OrderDto orderDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        orderId = UUID.randomUUID();
        orderDto = new OrderDto();
        orderDto.setId(orderId);
        orderDto.setOrderDate(LocalDateTime.now());
        orderDto.setStatus("PENDING");
        orderDto.setTotalAmount(200.0);
    }

    @Test
    void getAllOrders() {
        when(orderService.findAll()).thenReturn(Flux.just(orderDto));

        Flux<OrderDto> result = orderController.getAllOrders();

        assertNotNull(result);

        var orderList = result.collectList().block();

        assertNotNull(orderList);
        assertEquals(1, orderList.size());
        assertEquals(orderDto.getId(), orderList.get(0).getId());

        verify(orderService, times(1)).findAll();
    }

    @Test
    void getOrderById() {
        when(orderService.findById(orderId)).thenReturn(Mono.just(orderDto));

        OrderDto result = orderController.getOrderById(orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getId());
        assertEquals("PENDING", result.getStatus());

        verify(orderService, times(1)).findById(orderId);
    }

    @Test
    void createOrder() {
        when(orderService.save(orderDto)).thenReturn(Mono.just(orderDto));

        Mono<OrderDto> resultMono = orderController.createOrder(orderDto);

        assertNotNull(resultMono);
        OrderDto result = resultMono.block();
        assertNotNull(result);
        assertEquals(orderDto.getId(), result.getId());

        verify(orderService, times(1)).save(orderDto);
    }

    @Test
    void updateOrder() {
        when(orderService.update(orderId, orderDto)).thenReturn(Mono.just(orderDto));

        Mono<OrderDto> resultMono = orderController.updateOrder(orderId, orderDto);

        assertNotNull(resultMono);
        OrderDto result = resultMono.block();
        assertNotNull(result);
        assertEquals(orderId, result.getId());

        verify(orderService, times(1)).update(orderId, orderDto);
    }

    @Test
    void deleteOrder() {
        doNothing().when(orderService).delete(orderId);

        orderController.deleteOrder(orderId);

        verify(orderService, times(1)).delete(orderId);
    }
}