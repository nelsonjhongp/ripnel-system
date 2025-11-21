package com.ripnel.system.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderForm {

    private Long supplierId;
    private LocalDate orderedAt;
    private String notes;

    private List<PurchaseOrderItemForm> items = new ArrayList<>();

    // ================
    //  MÉTODO CLAVE
    // ================
    public void addEmptyItems(int n) {
        for (int i = 0; i < n; i++) {
            items.add(new PurchaseOrderItemForm());
        }
    }

    // GETTERS / SETTERS
    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public LocalDate getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(LocalDate orderedAt) {
        this.orderedAt = orderedAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<PurchaseOrderItemForm> getItems() {
        return items;
    }

    public void setItems(List<PurchaseOrderItemForm> items) {
        this.items = items;
    }
}
