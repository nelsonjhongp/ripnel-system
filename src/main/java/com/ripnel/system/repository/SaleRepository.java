package com.ripnel.system.repository;

import com.ripnel.system.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    // LISTADOS ORDENADOS POR FECHA REAL
    List<Sale> findTop20ByOrderByDateCreatedDesc();
    List<Sale> findTop5ByOrderByDateCreatedDesc();
    List<Sale> findTop50ByOrderByDateCreatedDesc();
    List<Sale> findAllByOrderByDateCreatedDesc();

    // TOTAL VENDIDO DESDE FECHA X
    @Query("SELECT SUM(s.totalAmount) FROM Sale s WHERE s.dateCreated >= :date")
    BigDecimal sumTotalAmountSince(@Param("date") LocalDateTime date);

    // TOP PRODUCTOS ÚLTIMOS X DÍAS
    @Query("""
        SELECT 
            CONCAT(v.product.name, ' · ', v.color.name, ' / ', v.size.code) AS label,
            SUM(i.quantity) AS totalQty
        FROM SaleItem i
        JOIN i.sale s
        JOIN i.variant v
        WHERE s.dateCreated >= :date
        GROUP BY label
        ORDER BY totalQty DESC
    """)
    List<Object[]> findTopProductsByQtySince(@Param("date") LocalDateTime date);

    // VENTAS DIARIAS PARA GRÁFICOS
    @Query("""
        SELECT DATE(s.dateCreated), SUM(s.totalAmount)
        FROM Sale s
        WHERE s.dateCreated >= :from
        GROUP BY DATE(s.dateCreated)
        ORDER BY DATE(s.dateCreated)
    """)
    List<Object[]> sumDailySalesSince(LocalDateTime from);
}
