package co.edu.unimagdalena.cbenavides.inventory.controller;

import co.edu.unimagdalena.cbenavides.inventory.dto.ItemDTO;
import co.edu.unimagdalena.cbenavides.inventory.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {
    private final ItemService itemService;

    @Autowired
    public InventoryController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("items")
    public Flux<ItemDTO> getAll() {
        return itemService.findAll();
    }

    @GetMapping("items/{id}")
    public Mono<ItemDTO> getFromId(@PathVariable("id") UUID id) {
        return itemService.findById(id);
    }

    @PostMapping("/")
    public Mono<ItemDTO> create(@RequestBody ItemDTO itemDTO) {
        return itemService.save(itemDTO);
    }

    @PutMapping("{id}")
    public Mono<ItemDTO> update(@PathVariable("id") UUID id, @RequestBody ItemDTO itemDTO) {
        return itemService.update(id, itemDTO);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") UUID id) {
        itemService.delete(id);
    }
}
