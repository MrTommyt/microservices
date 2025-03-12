package co.edu.unimagdalena.cbenavides.product.repository;


import co.edu.unimagdalena.cbenavides.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Override
    Optional<Product> findById(UUID id);

    @Override
    List<Product> findAll();

    Optional<Product> findByName(String name);
}
