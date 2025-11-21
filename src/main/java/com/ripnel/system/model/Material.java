package com.ripnel.system.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "materials")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    private String unit;

    @Column(name = "min_stock")
    private Double minStock;

    @Column(name = "stock_qty")
    private Double stockQty;

    @Column(name = "cost")
    private Double cost;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;   // 👈 OJO: este es tu entidad, NO java.util.function.Supplier

    private boolean active = true;

    @Column(name = "created_at")
    private Timestamp createdAt;

    // 🔹 GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getMinStock() {
        return minStock;
    }

    public void setMinStock(Double minStock) {
        this.minStock = minStock;
    }

    public Double getStockQty() {
        return stockQty;
    }

    public void setStockQty(Double stockQty) {
        this.stockQty = stockQty;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Supplier getSupplier() {   // 👈 usa tu entidad
        return supplier;
    }

    public void setSupplier(Supplier supplier) {   // 👈 firma correcta
        this.supplier = supplier;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
