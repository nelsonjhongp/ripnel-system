package com.ripnel.system.repository;

import com.ripnel.system.model.Product;
import com.ripnel.system.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    // dame las variantes activas de un producto ordenadas bonito
    List<ProductVariant> findByProductAndActiveTrueOrderByColor_NameAscSize_CodeAsc(Product product);
}
