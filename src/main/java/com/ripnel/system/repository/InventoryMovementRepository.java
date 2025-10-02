// repository/InventoryMovementRepository.java
package com.ripnel.system.repository;
import com.ripnel.system.model.InventoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {
    @Query("select count(m) from InventoryMovement m where m.createdAt >= :since")
    long countSince(LocalDateTime since);
    List<InventoryMovement> findTop5ByOrderByCreatedAtDesc();
}
