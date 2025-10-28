package com.ripnel.system.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_movements")
public class InventoryMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name="movement_type", nullable=false, length=20)
    private MovementType movementType; // IN, OUT, TRANSFER

    @ManyToOne(optional = false)
    @JoinColumn(name = "variant_id")
    private ProductVariant variant; // <- importante: ya no usamos sku suelto

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location; // desde / hacia dónde aplica este ajuste

    @ManyToOne
    @JoinColumn(name = "transfer_to_location_id")
    private Location transferToLocation;

    @Column(nullable=false)
    private Integer quantity;

    @Column(length=255)
    private String note;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public MovementType getMovementType() { return movementType; }
    public void setMovementType(MovementType movementType) { this.movementType = movementType; }

    public ProductVariant getVariant() { return variant; }
    public void setVariant(ProductVariant variant) { this.variant = variant; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public Location getTransferToLocation() {
        return transferToLocation;
    }

    public void setTransferToLocation(Location transferToLocation) {
        this.transferToLocation = transferToLocation;
    }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
