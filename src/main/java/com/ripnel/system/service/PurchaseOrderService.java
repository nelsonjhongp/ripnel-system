package com.ripnel.system.service;

import com.ripnel.system.dto.PurchaseOrderForm;
import com.ripnel.system.dto.PurchaseOrderItemForm;
import com.ripnel.system.model.*;
import com.ripnel.system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository orderRepo;

    @Autowired
    private PurchaseOrderItemRepository itemRepo;

    @Autowired
    private SupplierRepository supplierRepo;

    @Autowired
    private MaterialRepository materialRepo;

    @Autowired
    private MaterialRepository matRepo;

    // LISTAR
    public List<PurchaseOrder> findAll() {
        return orderRepo.findAllByOrderByOrderedAtDesc();
    }

    // BUSCAR
    public PurchaseOrder findById(Long id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("OC no encontrada"));
    }

    // GENERAR NÚMERO OC-000X
    private String generateNumber() {
        return orderRepo.findTopByOrderByIdDesc()
                .map(o -> {
                    long next = o.getId() + 1;
                    return String.format("OC-%04d", next);
                })
                .orElse("OC-0001");
    }

    // CREAR ORDEN
    public PurchaseOrder createOrder(PurchaseOrderForm form) {

        PurchaseOrder order = new PurchaseOrder();
        order.setNumber(generateNumber());
        order.setSupplier(supplierRepo.findById(form.getSupplierId()).orElseThrow());
        order.setOrderedAt(form.getOrderedAt() != null ? form.getOrderedAt() : LocalDate.now());
        order.setNotes(form.getNotes());
        order.setStatus(PurchaseOrderStatus.OPEN);

        // ITEMS
        form.getItems().forEach(i -> {
            if (i.getMaterialId() != null && i.getQty() != null && i.getQty() > 0) {

                PurchaseOrderItem item = new PurchaseOrderItem();
                item.setPurchaseOrder(order);
                item.setMaterial(materialRepo.findById(i.getMaterialId()).orElseThrow());
                item.setQty(i.getQty());
                item.setUnitCost(i.getUnitCost() != null ? i.getUnitCost() : 0.0);

                order.getItems().add(item);
            }
        });

        return orderRepo.save(order);
    }

    // RECIBIR ORDEN (ACTUALIZAR STOCK)
    public void receiveOrder(Long id) {

        PurchaseOrder order = findById(id);

        if (order.getStatus() == PurchaseOrderStatus.RECEIVED)
            return;

        for (PurchaseOrderItem it : order.getItems()) {
            Material mat = it.getMaterial();
            double newStock = (mat.getStockQty() != null ? mat.getStockQty() : 0) + it.getQty();
            mat.setStockQty(newStock);
            materialRepo.save(mat);

            it.setReceivedQty(it.getQty());
            itemRepo.save(it);
        }

        order.setStatus(PurchaseOrderStatus.RECEIVED);
        orderRepo.save(order);
    }
}
