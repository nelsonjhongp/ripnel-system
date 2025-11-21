package com.ripnel.system.repository;

import com.ripnel.system.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {

    @Query("select count(m) from Material m where m.active = true and m.stockQty < m.minStock")
    long countCriticalMaterials();

    @Query("select m from Material m where m.active = true and m.stockQty < m.minStock order by m.stockQty asc")
    List<Material> findTop5CriticalMaterials();
}
