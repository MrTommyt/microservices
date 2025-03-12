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
        return Mono.fromSupplier(() -> paymentRepository.save(paymentMapper.toPayment(paymentDto))).map(paymentMapper::toPaymentDto);
    }

    @Override
    public Mono<PaymentDto> update(UUID id, PaymentDto newPaymentDto) {
        return Mono.fromSupplier(() -> paymentRepository.findById(id).map(pay -> {
            pay.setId(id);
            pay.setAmount(newPaymentDto.getAmount());
            pay.setPaymentDate(newPaymentDto.getPaymentDate());
            pay.setPaymentMethod(newPaymentDto.getPaymentMethod());
            pay.setDescription(newPaymentDto.getDescription());

            return pay;
        }).map(paymentMapper::toPaymentDto).orElse(null));
    }

    @Override
    public void delete(UUID id) {
        paymentRepository.deleteById(id);
    }
}
