package co.edu.unimagdalena.cbenavides.order.controller;

import co.edu.unimagdalena.cbenavides.order.dto.OrderDto;
import co.edu.unimagdalena.cbenavides.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public Flux<OrderDto> getAllOrders() {
        return orderService.findAll();
    }

    @GetMapping("/orders/{id}")
    public OrderDto getOrderById(@PathVariable("id") UUID id) {
        return orderService.findById(id).block();
    }

    @PostMapping("/")
    public Mono<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        return orderService.save(orderDto);
    }

    @PutMapping("{id}")
    public Mono<OrderDto> updateOrder(@PathVariable("id") UUID id, @RequestBody OrderDto orderDto) {
        return orderService.update(id, orderDto);
    }

    @DeleteMapping("{id}")
    public void deleteOrder(@PathVariable("id") UUID id) {
        orderService.delete(id);
    }
}
