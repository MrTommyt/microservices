package co.edu.unimagdalena.cbenavides.inventory.dto;

import java.util.Objects;
import java.util.UUID;

public class ItemDTO {
    private UUID id;
    private UUID product;
    private int quantity;

    public ItemDTO(UUID id, UUID product, int quantity) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemDTO itemDTO)) return false;
        return getQuantity() == itemDTO.getQuantity() && Objects.equals(getId(),
            itemDTO.getId()) && Objects.equals(getProduct(), itemDTO.getProduct());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getProduct(), getQuantity());
    }

    @Override
    public String toString() {
        return "ItemDTO{" +
               "id=" + id +
               ", product=" + product +
               ", quantity=" + quantity +
               '}';
    }
}
