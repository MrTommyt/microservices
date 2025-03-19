package co.edu.unimagdalena.cbenavides.payments.service.impl;

import co.edu.unimagdalena.cbenavides.payments.dto.PaymentDto;
import co.edu.unimagdalena.cbenavides.payments.entity.Payment;
import co.edu.unimagdalena.cbenavides.payments.mapper.PaymentMapper;
import co.edu.unimagdalena.cbenavides.payments.repository.PaymentRepository;
import co.edu.unimagdalena.cbenavides.payments.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper = PaymentMapper.INSTANCE;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Flux<PaymentDto> findAll() {
        return Flux.fromIterable(paymentRepository.findAll()).map(paymentMapper::toPaymentDto);
    }

    @Override
    public Mono<PaymentDto> findById(UUID id) {
        return Mono.fromSupplier(() -> paymentRepository.findById(id).orElse(null)).map(paymentMapper::toPaymentDto);
    }

    @Override
    public Mono<PaymentDto> save(PaymentDto paymentDto) {
        return Mono.fromCallable(() -> paymentRepository.save(paymentMapper.toPayment(paymentDto)))
                .subscribeOn(Schedulers.boundedElastic())
                .map(paymentMapper::toPaymentDto);
    }

    @Override
    public Mono<PaymentDto> update(UUID id, PaymentDto newPaymentDto) {
        return Mono.fromCallable(() -> paymentRepository.findById(id).map(pay -> {
            pay = paymentMapper.toPayment(newPaymentDto);
            pay = paymentRepository.save(pay);

            return pay;
        }).map(paymentMapper::toPaymentDto).orElse(null));
    }

    @Override
    public void delete(UUID id) {
        paymentRepository.deleteById(id);
    }
}
