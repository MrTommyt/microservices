package co.edu.unimagdalena.cbenavides.order.service.impl;

import co.edu.unimagdalena.cbenavides.order.dto.OrderDto;
import co.edu.unimagdalena.cbenavides.order.mapper.OrderMapper;
import co.edu.unimagdalena.cbenavides.order.repository.OrderRepository;
import co.edu.unimagdalena.cbenavides.order.service.OrderService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper = OrderMapper.INSTANCE;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Flux<OrderDto> findAll() {
        return Flux.fromIterable(orderRepository.findAll()).map(orderMapper::toOrderDto);
    }

    @Override
    public Mono<OrderDto> findById(UUID id) {
        return Mono.fromSupplier(() -> orderRepository.findById(id).orElse(null)).map(orderMapper::toOrderDto);
    }

    @Override
    public Mono<OrderDto> save(OrderDto orderDto) {
        return Mono.fromSupplier(() -> orderRepository.save(orderMapper.toOrder(orderDto))).map(orderMapper::toOrderDto);
    }

    @Override
    public Mono<OrderDto> update(UUID id, OrderDto newOrderDto) {
        return Mono.fromSupplier(() -> orderRepository.findById(id).map(ord -> {
            ord.setId(id);
            ord.setOrderDate(newOrderDto.getOrderDate());
            ord.setStatus(newOrderDto.getStatus());
            ord.setTotalAmount(newOrderDto.getTotalAmount());

            return ord;
        }).map(orderMapper::toOrderDto).orElse(null));
    }

    @Override
    public void delete(UUID id) {
        orderRepository.deleteById(id);
    }
}
