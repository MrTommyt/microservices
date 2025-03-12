package co.edu.unimagdalena.cbenavides.payments.controller;

import co.edu.unimagdalena.cbenavides.payments.dto.PaymentDto;
import co.edu.unimagdalena.cbenavides.payments.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/payments")
    public Flux<PaymentDto> getAllPayments() {
        return paymentService.findAll();
    }

    @GetMapping("/payments/{id}")
    public PaymentDto getPaymentById(@PathVariable("id") UUID id) {
        return paymentService.findById(id).block();
    }

    @PostMapping("/")
    public Mono<PaymentDto> createPayment(@RequestBody PaymentDto paymentDto) {
        return paymentService.save(paymentDto);
    }

    @PutMapping("{id}")
    public Mono<PaymentDto> updatePayment(@PathVariable("id") UUID id, @RequestBody PaymentDto paymentDto) {
        return paymentService.update(id, paymentDto);
    }













}
