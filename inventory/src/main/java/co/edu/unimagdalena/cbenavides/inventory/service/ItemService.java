package co.edu.unimagdalena.cbenavides.inventory.service;

import co.edu.unimagdalena.cbenavides.inventory.dto.ItemDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface ItemService {
    Flux<ItemDTO> findAll();
    Mono<ItemDTO> findById(UUID id);
    Mono<ItemDTO> save(ItemDTO item);
    Mono<ItemDTO> update(UUID id, ItemDTO item);
    void delete(UUID id);
}
