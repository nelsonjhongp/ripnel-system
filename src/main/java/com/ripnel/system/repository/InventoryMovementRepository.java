package com.ripnel.system.repository;

import com.ripnel.system.model.InventoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {

    List<InventoryMovement> findTop20ByOrderByCreatedAtDesc();

    List<InventoryMovement> findTop5ByOrderByCreatedAtDesc();

    @Query("SELECT COUNT(m) FROM InventoryMovement m WHERE m.createdAt >= :since")
    long countSince(LocalDateTime since);

    // ❌ Eliminar esta línea porque InventoryMovement ya no tiene 'sku'
    // List<InventoryMovement> findBySkuOrderByCreatedAtDesc(String sku);
}

