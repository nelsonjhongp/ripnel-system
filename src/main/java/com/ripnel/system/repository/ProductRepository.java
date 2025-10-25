package com.ripnel.system.repository;

import com.ripnel.system.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // productos activos con stock menor al mínimo indicado
    @Query("SELECT p FROM Product p WHERE p.active = true AND p.stockQty < :minStock ORDER BY p.stockQty ASC")
    List<Product> findLowStock(int minStock);
}
