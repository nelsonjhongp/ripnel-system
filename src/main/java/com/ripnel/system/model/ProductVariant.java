package com.ripnel.system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(
        name = "product_variants",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"sku"})
        }
)
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // producto padre
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    // color
    @ManyToOne(optional = false)
    @JoinColumn(name = "color_id")
    private Color color;

    // talla
    @ManyToOne(optional = false)
    @JoinColumn(name = "size_id")
    private Size size;

    // SKU único, ej: "LEG-SUPLEX-BLK-M"
    @NotBlank
    @Column(nullable = false, unique = true, length = 80)
    private String sku;

    // precio específico opcional (si esta combinación cuesta diferente)
    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    // niveles para abastecimiento
    @Column(name = "min_level", precision = 12, scale = 2)
    private BigDecimal minLevel; // stock mínimo recomendado

    @Column(name = "lead_time_days")
    private Integer leadTimeDays; // días aprox. que demora reponer

    @Column(name = "safety_days")
    private Integer safetyDays; // colchón de seguridad

    // ACTIVO / INACTIVO (muy útil para no borrar una variante que ya no vendes)
    @Column(nullable = false)
    private Boolean active = true;

    // --- getters/setters ---

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

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getMinLevel() { return minLevel; }
    public void setMinLevel(BigDecimal minLevel) { this.minLevel = minLevel; }

    public Integer getLeadTimeDays() { return leadTimeDays; }
    public void setLeadTimeDays(Integer leadTimeDays) { this.leadTimeDays = leadTimeDays; }

    public Integer getSafetyDays() { return safetyDays; }
    public void setSafetyDays(Integer safetyDays) { this.safetyDays = safetyDays; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
