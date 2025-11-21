package com.ripnel.system.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "bom_items")
public class BillOfMaterials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Producto terminado (variant)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant productVariant;

    // Insumo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    // Cantidad de este material por 1 unidad de producto
    @Column(name = "qty_per_unit", nullable = false, precision = 15, scale = 2)
    private BigDecimal quantityPerUnit;

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public BigDecimal getQuantityPerUnit() {
        return quantityPerUnit;
    }

    public void setQuantityPerUnit(BigDecimal quantityPerUnit) {
        this.quantityPerUnit = quantityPerUnit;
    }
}
