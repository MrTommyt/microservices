package co.edu.unimagdalena.cbenavides.inventory.controller;

import co.edu.unimagdalena.cbenavides.inventory.dto.ItemDTO;
import co.edu.unimagdalena.cbenavides.inventory.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class InventoryControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private InventoryController inventoryController;

    private ItemDTO exampleItem;
    private UUID exampleId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        exampleId = UUID.randomUUID();

        exampleItem = new ItemDTO(exampleId, UUID.randomUUID(), 5);
//        exampleItem.setId(exampleId);
//        exampleItem.setProduct(UUID.randomUUID());
//        exampleItem.setQuantity(5);
    }

    @Test
    void getAll() {
        when(itemService.findAll()).thenReturn(Flux.just(exampleItem));

        Flux<ItemDTO> items = inventoryController.getAll();

        StepVerifier.create(items)
                .expectNext(exampleItem)
                .verifyComplete();

        verify(itemService, times(1)).findAll();
    }

    @Test
    void getFromId() {
        when(itemService.findById(exampleId)).thenReturn(Mono.just(exampleItem));

        Mono<ItemDTO> result = inventoryController.getFromId(exampleId);

        StepVerifier.create(result)
                .expectNext(exampleItem)
                .verifyComplete();

        verify(itemService, times(1)).findById(exampleId);
    }

    @Test
    void create() {
        when(itemService.save(any(ItemDTO.class))).thenReturn(Mono.just(exampleItem));

        Mono<ItemDTO> result = inventoryController.create(exampleItem);

        StepVerifier.create(result)
                .expectNext(exampleItem)
                .verifyComplete();

        verify(itemService, times(1)).save(exampleItem);
    }

    @Test
    void update() {
        when(itemService.update(exampleId, exampleItem)).thenReturn(Mono.just(exampleItem));

        Mono<ItemDTO> result = inventoryController.update(exampleId, exampleItem);

        StepVerifier.create(result)
                .expectNext(exampleItem)
                .verifyComplete();

        verify(itemService, times(1)).update(exampleId, exampleItem);
    }

    @Test
    void delete() {
        doNothing().when(itemService).delete(exampleId);

        inventoryController.delete(exampleId);

        verify(itemService, times(1)).delete(exampleId);
    }
}