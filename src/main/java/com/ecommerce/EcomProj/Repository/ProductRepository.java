package com.ecommerce.EcomProj.Repository;

import com.ecommerce.EcomProj.Model.Category;
import com.ecommerce.EcomProj.Model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategory(Pageable pageable, Category category);

    Page<Product> findByProductNameContainingIgnoreCase(Pageable pageable, String keyword);

    Product findByProductName(String productName);
}
