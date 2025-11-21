package com.ripnel.system.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "production_orders_v2")
public class ProductionOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // PO-0001, PO-0002...
    @Column(nullable = false, length = 20, unique = true)
    private String number;

    // Taller donde se produce (usamos Location como "workshop")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workshop_location_id", nullable = false)
    private Location workshop;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductionStatus status = ProductionStatus.PENDING;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    // Productos a fabricar
    @OneToMany(mappedBy = "productionOrder",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ProductionOrderItem> items = new ArrayList<>();

    // Registro histórico de consumo de materiales
    @OneToMany(mappedBy = "productionOrder",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<MaterialConsumption> consumptions = new ArrayList<>();

    // Helpers
    public void addItem(ProductionOrderItem item) {
        item.setProductionOrder(this);
        items.add(item);
    }

    public void addConsumption(MaterialConsumption mc) {
        mc.setProductionOrder(this);
        consumptions.add(mc);
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Location getWorkshop() {
        return workshop;
    }

    public void setWorkshop(Location workshop) {
        this.workshop = workshop;
    }

    public ProductionStatus getStatus() {
        return status;
    }

    public void setStatus(ProductionStatus status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<ProductionOrderItem> getItems() {
        return items;
    }

    public void setItems(List<ProductionOrderItem> items) {
        this.items = items;
    }

    public List<MaterialConsumption> getConsumptions() {
        return consumptions;
    }

    public void setConsumptions(List<MaterialConsumption> consumptions) {
        this.consumptions = consumptions;
    }
}
