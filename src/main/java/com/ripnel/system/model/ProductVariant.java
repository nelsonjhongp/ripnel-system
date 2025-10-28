package com.ripnel.system.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product_variants")
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // producto base (ej: "Leggings Suplex")
    @ManyToOne(optional=false)
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne(optional=false)
    @JoinColumn(name="color_id")
    private Color color;

    @ManyToOne(optional=false)
    @JoinColumn(name="size_id")
    private Size size;

    @Column(nullable=false, unique=true, length=80)
    private String sku;

    @Column(nullable=false)
    private Integer stockQty = 0;

    // precio de venta de ESTA variante
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price = BigDecimal.ZERO;

    // getters/setters ...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    public Size getSize() { return size; }
    public void setSize(Size size) { this.size = size; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Integer getStockQty() { return stockQty; }
    public void setStockQty(Integer stockQty) { this.stockQty = stockQty; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    @Column(nullable = false)
    private Boolean active = true;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

}
