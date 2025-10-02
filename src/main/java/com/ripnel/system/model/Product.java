package com.ripnel.system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity @Table(name="products", uniqueConstraints=@UniqueConstraint(columnNames="code"))
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Column(nullable=false, unique=true)
    private String code;

    @NotBlank @Column(nullable=false)
    private String name;

    @NotNull @Column(nullable=false, precision=12, scale=2)
    private BigDecimal price = BigDecimal.ZERO;

    @NotNull @Column(nullable=false)
    private Integer stockQty = 0;

    @ManyToOne(optional=false) @JoinColumn(name="category_id")
    private Category category;

    @Column(nullable=false)
    private Boolean active = true;

    // getters/setters
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockQty() {
        return stockQty;
    }

    public void setStockQty(Integer stockQty) {
        this.stockQty = stockQty;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
