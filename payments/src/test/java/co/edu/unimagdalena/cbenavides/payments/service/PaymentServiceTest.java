package co.edu.unimagdalena.cbenavides.payments.service;

import co.edu.unimagdalena.cbenavides.payments.dto.PaymentDto;
import co.edu.unimagdalena.cbenavides.payments.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentServiceImpl paymentServiceImpl;

    private PaymentDto payment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        payment = new PaymentDto(
                UUID.randomUUID(),
                20.0,
                LocalDateTime.now(),
                "Efective",
                "mm"
        );
    }

    @Test
    void findAll() {
        // no son comentarios de chatgpt, son mios pa entender...att:mary
        // Servicio Mockeado
        Mockito.when(paymentService.findAll()).thenReturn(Flux.just(payment));

        // We call the actual method of service
        Flux<PaymentDto> result = paymentService.findAll();

        // convert to a list
        List<PaymentDto> payments = result.collectList().block();

        // validate that the list is not null or empty
        assertNotNull(payments);
        assertFalse(payments.isEmpty());

        // validate that this is the content
        assertEquals(payments.size(), 1);
        assertEquals(payments.get(0).getId(), payment.getId());
    }

    @Test
    void findById() {
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}