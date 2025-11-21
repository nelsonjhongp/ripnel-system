package com.ripnel.system.repository;

import com.ripnel.system.model.PurchaseOrder;
import com.ripnel.system.model.PurchaseOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    List<PurchaseOrder> findAllByOrderByOrderedAtDesc();

    Optional<PurchaseOrder> findTopByOrderByIdDesc();

    long countByStatus(PurchaseOrderStatus status);

    List<PurchaseOrder> findTop5ByStatusOrderByOrderedAtAsc(PurchaseOrderStatus status);
}
