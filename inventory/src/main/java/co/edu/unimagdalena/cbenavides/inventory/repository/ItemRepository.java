package co.edu.unimagdalena.cbenavides.inventory.repository;

import co.edu.unimagdalena.cbenavides.inventory.entity.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemRepository extends MongoRepository<Item, UUID> {
    @Override
    Optional<Item> findById(UUID uuid);
    @Override
    List<Item> findAll();

}
