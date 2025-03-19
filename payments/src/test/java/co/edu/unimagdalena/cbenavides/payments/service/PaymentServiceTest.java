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
import reactor.core.publisher.Mono;

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
        UUID id = payment.getId();

        // cuando se llame .findById entonces retorna un mono que contiene el objeto payment
        Mockito.when(paymentService.findById(id)).thenReturn(Mono.just(payment));

        Mono<PaymentDto> result = paymentService.findById(id);
        // como Mono es reactiva entonces se usa .block para obtener su valor de forma sincronica
        PaymentDto paymentDto = result.block();

        assertNotNull(paymentDto);
        assertEquals(paymentDto.getId(), payment.getId());
    }

    @Test
    void save() {
        Mockito.when(paymentService.save(payment)).thenReturn(Mono.just(payment));

        Mono<PaymentDto> result = paymentService.save(payment);
        PaymentDto paymentDto = result.block();

        assertNotNull(paymentDto);
        assertEquals(paymentDto.getId(), payment.getId());
    }

    @Test
    void update() {
        UUID id = payment.getId();
        PaymentDto updatedPayment = new PaymentDto(id, 10.0, LocalDateTime.now(), "Credit", "mm");

        Mockito.when(paymentService.update(id, updatedPayment)).thenReturn(Mono.just(updatedPayment));

        Mono<PaymentDto> result = paymentService.update(id, updatedPayment);
        PaymentDto paymentDto = result.block();

        assertNotNull(paymentDto);
        assertEquals(10.0, paymentDto.getAmount());
        assertEquals(paymentDto.getId(), payment.getId());
    }

    @Test
    void delete() {
        UUID id = payment.getId();
        Mockito.doNothing().when(paymentService).delete(id);

        assertDoesNotThrow( () -> paymentService.delete(id));

        //aaaa
        //aaaa
    }
}