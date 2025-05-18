package co.edu.unimagdalena.cbenavides.payments.controller;

import co.edu.unimagdalena.cbenavides.payments.dto.PaymentDto;
import co.edu.unimagdalena.cbenavides.payments.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

    private PaymentService paymentService;
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        paymentService = mock(PaymentService.class);
        paymentController = new PaymentController(paymentService);
    }

    @Test
    void getAllPayments() {
        PaymentDto dto1 = new PaymentDto();
        PaymentDto dto2 = new PaymentDto();
        when(paymentService.findAll()).thenReturn(Flux.just(dto1, dto2));

        Flux<PaymentDto> results = paymentController.getAllPayments();

        assertEquals(2, results.collectList().block().size());
        verify(paymentService, times(1)).findAll();
    }

    @Test
    void getPaymentById() {
        UUID id = UUID.randomUUID();
        PaymentDto dto = new PaymentDto();
        when(paymentService.findById(id)).thenReturn(Mono.just(dto));

        PaymentDto result = paymentController.getPaymentById(id);

        assertNotNull(result);
        verify(paymentService, times(1)).findById(id);
    }

    @Test
    void createPayment() {
        PaymentDto dto = new PaymentDto();
        when(paymentService.save(dto)).thenReturn(Mono.just(dto));

        Mono<PaymentDto> result = paymentController.createPayment(dto);

        assertEquals(dto, result.block());
        verify(paymentService, times(1)).save(dto);
    }

    @Test
    void updatePayment() {
        UUID id = UUID.randomUUID();
        PaymentDto dto = new PaymentDto();

        when(paymentService.update(id, dto)).thenReturn(Mono.just(dto));

        Mono<PaymentDto> result = paymentController.updatePayment(id, dto);

        assertNotNull(result);
        assertEquals(dto, result.block());
        verify(paymentService, times(1)).update(id, dto);
    }

    @Test
    void deletePayment() {
        UUID id = UUID.randomUUID();
        doNothing().when(paymentService).delete(id);

        paymentController.deletePayment(id);

        verify(paymentService, times(1)).delete(id);
    }
}