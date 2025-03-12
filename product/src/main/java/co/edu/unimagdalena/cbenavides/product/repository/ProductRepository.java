package co.edu.unimagdalena.cbenavides.product.repository;

import co.edu.unimagdalena.cbenavides.product.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends MongoRepository<Product, UUID> {
    Optional<Product> findAllById(UUID id);
    List<Product> findAll();
    Optional<Product> findByName(String name);
}
