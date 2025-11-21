package com.ripnel.system.repository;

import com.ripnel.system.model.ProductionOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductionOrderRepository extends JpaRepository<ProductionOrder, Long> {

    Optional<ProductionOrder> findTopByOrderByIdDesc();
}
