package com.ripnel.system.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="inventory_movements")
public class InventoryMovement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING) @Column(nullable=false)
    private MovementType type;

    @ManyToOne(optional=false) @JoinColumn(name="product_id")
    private Product product;

    @Column(nullable=false)
    private Integer quantity;

    @Column(nullable=false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    public void prePersist(){
        if (createdAt == null) createdAt = java.time.LocalDateTime.now();
    }

    private String note;

    // getters/setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MovementType getType() {
        return type;
    }

    public void setType(MovementType type) {
        this.type = type;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
