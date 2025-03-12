package co.edu.unimagdalena.cbenavides.payments.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentDto {

    private UUID id;
    private Double amount;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private String description;

    public PaymentDto() {
    }

    public PaymentDto(UUID id, Double amount, LocalDateTime paymentDate, String paymentMethod, String description) {
        this.id = id;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
