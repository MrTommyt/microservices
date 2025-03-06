package co.edu.unimagdalena.cbenavides.product.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.rmi.server.UID;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "category")
    private String category;

    @Column(name = "description")
    private String description;

}
