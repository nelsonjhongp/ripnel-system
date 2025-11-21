package com.ripnel.system.repository;

import com.ripnel.system.model.BillOfMaterials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillOfMaterialsRepository extends JpaRepository<BillOfMaterials, Long> {

    List<BillOfMaterials> findByProductVariantId(Long variantId);
}
