package com.ripnel.system.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_movements")
public class InventoryMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // cuándo pasó
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // tipo: IN / OUT / TRANSFER
    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 20)
    private MovementType movementType;

    // qué SKU tocamos (ej: "LEG-SUPLEX-BLK-M")
    @Column(name = "sku", nullable = false, length = 80)
    private String sku;

    // a qué local le afecta este stock
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    // cuántas unidades entraron o salieron
    @Column(nullable = false)
    private Integer quantity;

    // nota tipo "recepción de proveedor", "salida para vitrina", etc
    @Column(length = 255)
    private String note;

    // getters/setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public MovementType getMovementType() { return movementType; }
    public void setMovementType(MovementType movementType) { this.movementType = movementType; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
