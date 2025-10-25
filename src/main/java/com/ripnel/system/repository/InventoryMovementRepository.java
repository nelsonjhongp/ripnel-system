package com.ripnel.system.repository;

import com.ripnel.system.model.InventoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {

    // Para mostrar los últimos movimientos primero
    List<InventoryMovement> findTop20ByOrderByCreatedAtDesc();

    // Para mostrar los últimos 5 en el dashboard admin
    List<InventoryMovement> findTop5ByOrderByCreatedAtDesc();

    // 💡 NUEVO: contar cuántos movimientos hubo desde una fecha dada
    @Query("SELECT COUNT(m) FROM InventoryMovement m WHERE m.createdAt >= :since")
    long countSince(@Param("since") LocalDateTime since);
}
