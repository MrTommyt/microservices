package co.edu.unimagdalena.cbenavides.order.service;

import co.edu.unimagdalena.cbenavides.order.dto.OrderDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrderService {
    Flux<OrderDto> findAll();
    Mono<OrderDto> findById(UUID id);
    Mono<OrderDto> save(OrderDto orderDto);
    Mono<OrderDto> update(UUID id, OrderDto newOrderDto);
    void delete(UUID id);
}
