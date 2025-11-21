package com.ripnel.system.repository;

import com.ripnel.system.model.Product;
import com.ripnel.system.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    @Query("select coalesce(sum(v.stockQty), 0) from ProductVariant v")
    Long sumGlobalStockQty();

    List<ProductVariant> findTop5ByStockQtyLessThanEqualOrderByStockQtyAsc(Long qty);

    List<ProductVariant> findByProductAndActiveTrueOrderByColor_NameAscSize_CodeAsc(Product product);

    @Query("SELECT v.product.category.name, SUM(v.stockQty) " +
            "FROM ProductVariant v " +
            "GROUP BY v.product.category.name " +
            "ORDER BY 2 DESC")
    List<Object[]> getStockByCategory();
}