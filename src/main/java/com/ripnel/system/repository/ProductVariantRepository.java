package com.ripnel.system.repository;

import com.ripnel.system.model.Product;
import com.ripnel.system.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    ProductVariant findBySku(String sku);

    List<ProductVariant> findByActiveTrue();

    List<ProductVariant> findByProductAndActiveTrueOrderByColor_NameAscSize_CodeAsc(Product product);
}
