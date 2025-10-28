package com.ripnel.system.service;

import com.ripnel.system.model.InventoryMovement;
import com.ripnel.system.model.Location;
import com.ripnel.system.model.MovementType;
import com.ripnel.system.model.ProductVariant;
import com.ripnel.system.repository.InventoryMovementRepository;
import com.ripnel.system.repository.ProductVariantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class InventoryService {

    private final InventoryMovementRepository movementRepo;
    private final ProductVariantRepository variantRepo;

    public InventoryService(
            InventoryMovementRepository movementRepo,
            ProductVariantRepository variantRepo
    ) {
        this.movementRepo = movementRepo;
        this.variantRepo = variantRepo;
    }

    /**
     * Registra un movimiento de inventario y actualiza stock.
     *
     * Soportado:
     *  - IN: entra stock a un local
     *  - OUT: sale stock de un local
     *  - TRANSFER: mueve stock de un local origen a un local destino
     *
     * Reglas:
     *  - Nunca dejamos stock negativo.
     *  - Siempre persistimos historial en inventory_movements.
     *
     * Parámetros que deben venir en 'mov':
     *   mov.getVariant().id        -> variante afectada
     *   mov.getQuantity()          -> cantidad (positivo)
     *   mov.getMovementType()      -> IN / OUT / TRANSFER
     *   mov.getLocation()          -> local origen (para IN/OUT y también para TRANSFER = origen)
     *   mov.getNote()              -> nota opcional
     *   mov.getCreatedAt()         -> si viene null lo rellenamos nosotros
     *
     * Para TRANSFER:
     *   - Usamos mov.getTransferToLocation() como destino
     *   - (asegúrate de tener ese campo en InventoryMovement si quieres soportar transfer)
     */
    @Transactional
    public void registerMovement(InventoryMovement mov) {

        // --- Validaciones básicas ---
        if (mov.getVariant() == null || mov.getVariant().getId() == null) {
            throw new IllegalArgumentException("Falta variant en el movimiento");
        }
        if (mov.getQuantity() == null || mov.getQuantity() <= 0) {
            throw new IllegalArgumentException("Cantidad inválida");
        }
        if (mov.getMovementType() == null) {
            throw new IllegalArgumentException("Tipo de movimiento inválido");
        }
        if (mov.getLocation() == null) {
            throw new IllegalArgumentException("Falta ubicación origen");
        }

        // Asegurar timestamp
        if (mov.getCreatedAt() == null) {
            mov.setCreatedAt(LocalDateTime.now());
        }

        // Traer la variante "real" de BD (estado actual, stock actual)
        ProductVariant variantDB = variantRepo.findById(mov.getVariant().getId())
                .orElseThrow(() -> new IllegalArgumentException("Variante no encontrada"));

        int qty = mov.getQuantity();

        // --- Lógica por tipo ---
        if (mov.getMovementType() == MovementType.IN) {
            // stock sube
            int newStock = safeAdd(variantDB.getStockQty(), qty);
            variantDB.setStockQty(newStock);

            // guardamos cambios
            variantRepo.save(variantDB);
            movementRepo.save(mov);
            return;
        }

        if (mov.getMovementType() == MovementType.OUT) {
            // stock baja
            int newStock = safeSubtract(variantDB.getStockQty(), qty);
            variantDB.setStockQty(newStock);

            variantRepo.save(variantDB);
            movementRepo.save(mov);
            return;
        }

        if (mov.getMovementType() == MovementType.TRANSFER) {

            // Para soportar TRANSFER necesitamos un destino
            Location destino = mov.getTransferToLocation();
            if (destino == null) {
                throw new IllegalArgumentException("Falta ubicación destino para TRANSFER");
            }

            // 1. sacar del origen (OUT)
            InventoryMovement salida = new InventoryMovement();
            salida.setCreatedAt(mov.getCreatedAt());
            salida.setMovementType(MovementType.OUT);
            salida.setVariant(variantDB);
            salida.setQuantity(qty);
            salida.setLocation(mov.getLocation()); // origen
            salida.setNote("TRANSFER OUT -> " + destino.getName());

            int newStockOrigen = safeSubtract(variantDB.getStockQty(), qty);
            variantDB.setStockQty(newStockOrigen);
            variantRepo.save(variantDB);
            movementRepo.save(salida);

            // 2. entrar al destino (IN)
            InventoryMovement entrada = new InventoryMovement();
            entrada.setCreatedAt(mov.getCreatedAt());
            entrada.setMovementType(MovementType.IN);
            entrada.setVariant(variantDB);
            entrada.setQuantity(qty);
            entrada.setLocation(destino); // destino
            entrada.setNote("TRANSFER IN <- " + mov.getLocation().getName());

            int newStockDestino = safeAdd(variantDB.getStockQty(), qty);
            variantDB.setStockQty(newStockDestino);
            variantRepo.save(variantDB);
            movementRepo.save(entrada);

            return;
        }

        // Si llega un tipo que no reconocemos
        throw new IllegalArgumentException("Tipo de movimiento no soportado: " + mov.getMovementType());
    }

    // ayuda: suma evitando null
    private int safeAdd(Integer base, int delta) {
        int b = (base == null ? 0 : base);
        return b + delta;
    }

    // ayuda: resta evitando null y evitando negativo
    private int safeSubtract(Integer base, int delta) {
        int b = (base == null ? 0 : base);
        int r = b - delta;
        return (r < 0 ? 0 : r);
    }
}
