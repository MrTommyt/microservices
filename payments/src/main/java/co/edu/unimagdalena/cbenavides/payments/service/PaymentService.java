package co.edu.unimagdalena.cbenavides.payments.service;

import co.edu.unimagdalena.cbenavides.payments.dto.PaymentDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PaymentService {
    Flux<PaymentDto> findAll();
    Mono<PaymentDto> findById(UUID id);
    Mono<PaymentDto> save(PaymentDto paymentDto);
    Mono<PaymentDto> update(UUID id, PaymentDto newPaymentDto);
    void delete(UUID id);
}
