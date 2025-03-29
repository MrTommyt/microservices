package co.edu.unimagdalena.cbenavides.inventory.entity;

import org.springframework.data.annotation.Id;

import java.util.UUID;

public class Item {
    @Id
    private UUID id;

    private UUID product;

    private int quantity;

    public Item(UUID id, UUID product, int quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProduct() {
        return product;
    }

    public void setProduct(UUID product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
