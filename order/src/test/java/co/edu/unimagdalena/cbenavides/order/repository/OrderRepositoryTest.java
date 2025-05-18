package co.edu.unimagdalena.cbenavides.order.repository;

import org.junit.jupiter.api.BeforeEach;
import co.edu.unimagdalena.cbenavides.order.entity.Order;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderRepositoryTest {

    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
    }

    @Test
    void findById() {
        UUID id = UUID.randomUUID();

        Order order = new Order();
        order.setId(id);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setTotalAmount(100.0);

        when(orderRepository.findById(id)).thenReturn(Optional.of(order));

        Optional<Order> result = orderRepository.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        assertEquals("PENDING", order.getStatus());
        assertEquals(100.0, order.getTotalAmount());

        verify(orderRepository, times(1)).findById(id);
    }

    @Test
    void findAll() {
        Order order1 = new Order();
        order1.setId(UUID.randomUUID());
        order1.setOrderDate(LocalDateTime.now());
        order1.setStatus("PENDING");
        order1.setTotalAmount(100.0);

        Order order2 = new Order();
        order2.setId(UUID.randomUUID());
        order2.setOrderDate(LocalDateTime.now());
        order2.setStatus("SHIPPED");
        order2.setTotalAmount(200.0);

        when(orderRepository.findAll()).thenReturn(List.of(order1, order2));

        List<Order> result = orderRepository.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("PENDING", result.get(0).getStatus());
        assertEquals(100.0, result.get(0).getTotalAmount());

        assertEquals("SHIPPED", result.get(1).getStatus());
        assertEquals(200.0, result.get(1).getTotalAmount());

        verify(orderRepository, times(1)).findAll();
    }
}