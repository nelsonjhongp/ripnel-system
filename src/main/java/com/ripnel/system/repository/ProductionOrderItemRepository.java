package com.ripnel.system.repository;

import com.ripnel.system.model.ProductionOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionOrderItemRepository extends JpaRepository<ProductionOrderItem, Long> {
}
