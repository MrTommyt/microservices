package co.edu.unimagdalena.cbenavides.order.repository;

import co.edu.unimagdalena.cbenavides.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Override
    Optional<Order> findById(UUID uuid);

    @Override
    List<Order> findAll();
}
