package com.ripnel.system.service;

import com.ripnel.system.model.*;
import com.ripnel.system.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {
    private final ProductRepository productRepo;
    private final InventoryMovementRepository movementRepo;

    public InventoryService(ProductRepository productRepo, InventoryMovementRepository movementRepo) {
        this.productRepo = productRepo;
        this.movementRepo = movementRepo;
    }

    @Transactional
    public InventoryMovement registerMovement(Long productId, MovementType type, int qty, String note) {
        var product = productRepo.findById(productId).orElseThrow();
        int newStock = type == MovementType.IN ? product.getStockQty() + qty : product.getStockQty() - qty;
        if (newStock < 0) throw new IllegalArgumentException("Stock insuficiente");
        product.setStockQty(newStock);
        var mov = new InventoryMovement();
        mov.setProduct(product);
        mov.setType(type);
        mov.setQuantity(qty);
        mov.setNote(note);
        productRepo.save(product);
        return movementRepo.save(mov);
    }
}
