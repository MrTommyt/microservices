package co.edu.unimagdalena.cbenavides.payments.repository;

import co.edu.unimagdalena.cbenavides.payments.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    @Override
    Optional<Payment> findById(UUID uuid);

    @Override
    List<Payment> findAll();
}
