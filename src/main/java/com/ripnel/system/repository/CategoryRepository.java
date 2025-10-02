// repository/CategoryRepository.java
package com.ripnel.system.repository;
import com.ripnel.system.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CategoryRepository extends JpaRepository<Category, Long> {}
