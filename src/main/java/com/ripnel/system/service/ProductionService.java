package com.ripnel.system.service;

import com.ripnel.system.model.*;
import com.ripnel.system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ProductionService {

    @Autowired
    private ProductionOrderRepository orderRepo;

    @Autowired
    private ProductionOrderItemRepository itemRepo;

    @Autowired
    private BillOfMaterialsRepository bomRepo;

    @Autowired
    private MaterialRepository materialRepo;

    @Autowired
    private MaterialConsumptionRepository consumptionRepo;

    @Autowired
    private InventoryService inventoryService;

    // =========================
    //  NÚMERO PO-000X
    // =========================
    public String generateNumber() {
        int next = orderRepo.findTopByOrderByIdDesc()
                .map(o -> o.getId().intValue() + 1)
                .orElse(1);

        return String.format("PO-%04d", next);
    }

    // =========================
    //  CREAR ORDEN
    // =========================
    public ProductionOrder createOrder(ProductionOrder order) {
        order.setNumber(generateNumber());
        order.setStatus(ProductionStatus.PENDING);
        if (order.getStartDate() == null) {
            order.setStartDate(LocalDate.now());
        }
        return orderRepo.save(order);
    }

    // =========================
    //  INICIAR PRODUCCIÓN
    // =========================
    public void startProduction(Long orderId) {
        ProductionOrder order = orderRepo.findById(orderId).orElseThrow();
        order.setStatus(ProductionStatus.IN_PROGRESS);
        orderRepo.save(order);
    }

    // =========================
    //  CONSUMIR MATERIALES
    // =========================
    public void consumeMaterials(Long orderId) {

        ProductionOrder order = orderRepo.findById(orderId).orElseThrow();

        for (ProductionOrderItem item : order.getItems()) {

            List<BillOfMaterials> bomList =
                    bomRepo.findByProductVariantId(item.getProduct().getId());

            for (BillOfMaterials b : bomList) {

                BigDecimal total = b.getQuantityPerUnit().multiply(item.getQuantity());
                Material material = b.getMaterial();

                // 1. Actualizar stock de Material
                Double oldStock = material.getStockQty() == null ? 0.0 : material.getStockQty();
                Double newStock = oldStock - total.doubleValue();
                if (newStock < 0) {
                    newStock = 0.0; // nunca negativo
                }
                material.setStockQty(newStock);
                materialRepo.save(material);

                // 2. Registrar consumo histórico
                MaterialConsumption mc = new MaterialConsumption();
                mc.setMaterial(material);
                mc.setQuantityUsed(total);
                mc.setProductionOrder(order);
                consumptionRepo.save(mc);
            }
        }
    }

    // =========================
    //  FINALIZAR PRODUCCIÓN
    // =========================
    public void finishProduction(Long orderId) {

        ProductionOrder order = orderRepo.findById(orderId).orElseThrow();
        Location workshop = order.getWorkshop();

        for (ProductionOrderItem item : order.getItems()) {

            ProductVariant variant = item.getProduct();

            InventoryMovement mov = new InventoryMovement();
            mov.setVariant(variant);
            mov.setMovementType(MovementType.IN);
            mov.setLocation(workshop);
            mov.setQuantity(item.getQuantity().intValue());
            mov.setNote("Producto terminado " + order.getNumber());

            inventoryService.registerMovement(mov);
        }

        order.setStatus(ProductionStatus.FINISHED);
        order.setEndDate(LocalDate.now());
        orderRepo.save(order);
    }
}
