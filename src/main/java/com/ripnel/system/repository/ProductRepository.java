// repository/ProductRepository.java
package com.ripnel.system.repository;
import com.ripnel.system.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProductRepository extends JpaRepository<Product, Long> {}
