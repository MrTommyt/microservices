package co.edu.unimagdalena.cbenavides.payments.repository;

import co.edu.unimagdalena.cbenavides.payments.entity.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

class PaymentRepositoryTest {

    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        paymentRepository = mock(PaymentRepository.class);
    }

    @Test
    void findById() {
        UUID paymentId = UUID.randomUUID();
        Payment payment = new Payment();
        payment.setId(paymentId);
        payment.setAmount(100.0);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setDescription("pay");

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        Optional<Payment> result = paymentRepository.findById(paymentId);

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get());
        assertEquals(100.0, result.get().getAmount(), 0.0001);
        assertEquals("pay", result.get().getDescription());

        verify(paymentRepository, times(1)).findById(paymentId);
    }

    @Test
    void findAll() {
        Payment p1 = new Payment();
        p1.setId(UUID.randomUUID());
        p1.setAmount(100.0);
        p1.setPaymentDate(LocalDateTime.now());
        p1.setPaymentMethod("Credit Card");
        p1.setDescription("Pay 1");

        Payment p2 = new Payment();
        p2.setId(UUID.randomUUID());
        p2.setAmount(170.0);
        p2.setPaymentDate(LocalDateTime.now());
        p2.setPaymentMethod("Credit Card");
        p2.setDescription("Pay 2");

        when(paymentRepository.findAll()).thenReturn(java.util.List.of(p1, p2));

        var result = paymentRepository.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Pay 1", result.get(0).getDescription());
        assertEquals("Pay 2", result.get(1).getDescription());
        assertEquals("Credit Card", result.get(0).getPaymentMethod());
        assertEquals("Credit Card", result.get(1).getPaymentMethod());

        verify(paymentRepository, times(1)).findAll();

    }
}