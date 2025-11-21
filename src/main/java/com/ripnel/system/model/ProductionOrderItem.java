package com.ripnel.system.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "production_order_items")
public class ProductionOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Orden madre
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "production_order_id", nullable = false)
    private ProductionOrder productionOrder;

    // Producto terminado a fabricar
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant product;

    // Cantidad a producir
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal quantity;

    // Opcional: talla textual si quieres (aunque ya la tiene en variant->size)
    @Column(length = 20)
    private String sizeLabel;

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductionOrder getProductionOrder() {
        return productionOrder;
    }

    public void setProductionOrder(ProductionOrder productionOrder) {
        this.productionOrder = productionOrder;
    }

    public ProductVariant getProduct() {
        return product;
    }

    public void setProduct(ProductVariant product) {
        this.product = product;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getSizeLabel() {
        return sizeLabel;
    }

    public void setSizeLabel(String sizeLabel) {
        this.sizeLabel = sizeLabel;
    }
}
