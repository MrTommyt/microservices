package co.edu.unimagdalena.cbenavides.inventory.service.impl;

import co.edu.unimagdalena.cbenavides.inventory.dto.ItemDTO;
import co.edu.unimagdalena.cbenavides.inventory.mapper.ItemMapper;
import co.edu.unimagdalena.cbenavides.inventory.repository.ItemRepository;
import co.edu.unimagdalena.cbenavides.inventory.service.ItemService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final ItemMapper mapper = ItemMapper.INSTANCE;

    public ItemServiceImpl(ItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<ItemDTO> findAll() {
        return Flux.fromIterable(repository.findAll())
            .map(mapper::toItemDTO);
    }

    @Override
    public Mono<ItemDTO> findById(UUID id) {
        return Mono.fromSupplier(() -> repository.findById(id).orElse(null))
            .map(mapper::toItemDTO);
    }

    @Override
    public Mono<ItemDTO> save(ItemDTO item) {
        return Mono.fromCallable(() -> repository.save(mapper.toItem(item)))
                .subscribeOn(Schedulers.boundedElastic())
                .map(mapper::toItemDTO);
    }

    @Override
    public Mono<ItemDTO> update(UUID id, ItemDTO item) {
        return Mono.fromCallable(() -> repository.findById(id).map(it -> {
            it = mapper.toItem(item);
            it = repository.save(it);

            return it;
        }).map(mapper::toItemDTO).orElse(null));
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
